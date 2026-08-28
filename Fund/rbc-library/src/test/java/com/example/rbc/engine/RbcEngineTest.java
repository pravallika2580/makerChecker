package com.example.rbc.engine;

import com.example.rbc.model.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RbcEngineTest {
    private final RbcEngine engine = new RbcEngine();
    private Rule limitRule(String role, String max) {
        return Rule.builder().role(role).action("FUND_TRANSFER").resource("FUND_TRANSFER")
                .condition(new Condition("amount", ComparisonOperator.LESS_THAN_OR_EQUAL, new BigDecimal(max))).build();
    }
    private AccessRequest transfer(String role, String amount) {
        return AccessRequest.builder().role(role).action("FUND_TRANSFER").resource("FUND_TRANSFER")
                .attribute("amount", new BigDecimal(amount)).build();
    }
    @Test void allowsAmountBelowLimit() { assertTrue(engine.check(transfer("MAKER2", "4000"), List.of(limitRule("MAKER2", "5000"))).isAllowed()); }
    @Test void allowsAmountAtLimit() { assertTrue(engine.check(transfer("MAKER2", "5000"), List.of(limitRule("MAKER2", "5000"))).isAllowed()); }
    @Test void deniesAmountAboveLimit() { assertFalse(engine.check(transfer("MAKER2", "5001"), List.of(limitRule("MAKER2", "5000"))).isAllowed()); }
    @Test void allowsMakerThreeAtLimit() { assertTrue(engine.check(transfer("MAKER3", "10000"), List.of(limitRule("MAKER3", "10000"))).isAllowed()); }
    @Test void deniesMakerThreeAboveLimit() { assertFalse(engine.check(transfer("MAKER3", "10001"), List.of(limitRule("MAKER3", "10000"))).isAllowed()); }
    @Test void allowsUnconditionalRule() {
        Rule rule = Rule.builder().role("MAKER1").action("CREATE").resource("BENEFICIARY").build();
        assertTrue(engine.check(AccessRequest.builder().role("MAKER1").action("CREATE").resource("BENEFICIARY").build(), List.of(rule)).isAllowed());
    }
    @Test void deniesWhenNoRuleMatches() { assertFalse(engine.check(AccessRequest.builder().role("USER").action("DELETE").resource("ACCOUNT").build(), List.of()).isAllowed()); }
    @Test void supportsEveryOperator() {
        assertTrue(new Condition("v", ComparisonOperator.EQUALS, new BigDecimal("5.0")).matches(new BigDecimal("5")));
        assertTrue(new Condition("v", ComparisonOperator.NOT_EQUALS, 6).matches(5));
        assertTrue(new Condition("v", ComparisonOperator.LESS_THAN, 6).matches(5));
        assertTrue(new Condition("v", ComparisonOperator.LESS_THAN_OR_EQUAL, 5).matches(5));
        assertTrue(new Condition("v", ComparisonOperator.GREATER_THAN, 4).matches(5));
        assertTrue(new Condition("v", ComparisonOperator.GREATER_THAN_OR_EQUAL, 5).matches(5));
    }
    @Test void higherPriorityRulesGovernEvaluation() {
        Rule lower = limitRule("MAKER2", "10000");
        Rule higher = Rule.builder().role("MAKER2").action("FUND_TRANSFER").resource("FUND_TRANSFER").priority(1)
                .condition(new Condition("amount", ComparisonOperator.LESS_THAN_OR_EQUAL, new BigDecimal("5000"))).build();
        assertFalse(engine.check(transfer("MAKER2", "7000"), List.of(lower, higher)).isAllowed());
    }
}
