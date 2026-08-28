# RBC Library

A framework-independent Java 17 authorization evaluator. It knows only strings and generic attributes; roles, actions, resources, and rules belong to the consuming application.

## Evaluation policy

The engine first matches `role`, `action`, and `resource`. It then keeps only rules with the highest priority (larger integer wins). Within that priority, any rule whose conditions all pass allows access. If no rule matches, or none of the governing rules pass, access is denied. This makes conflicts deterministic and prevents a lower-priority rule from unexpectedly bypassing a higher-priority constraint.

## Standalone use

```java
RbcEngine engine = new RbcEngine();
Rule rule = Rule.builder()
    .role("MAKER2").action("FUND_TRANSFER").resource("FUND_TRANSFER")
    .condition(new Condition("amount", ComparisonOperator.LESS_THAN_OR_EQUAL,
        new BigDecimal("5000")))
    .build();

AccessDecision allowed = engine.check(AccessRequest.builder()
    .role("MAKER2").action("FUND_TRANSFER").resource("FUND_TRANSFER")
    .attribute("amount", new BigDecimal("4500")).build(), List.of(rule));
// allowed.isAllowed() == true

AccessDecision denied = engine.check(AccessRequest.builder()
    .role("MAKER2").action("FUND_TRANSFER").resource("FUND_TRANSFER")
    .attribute("amount", new BigDecimal("7000")).build(), List.of(rule));
// denied.isAllowed() == false; denied.getReason() explains why
```

An application can implement `RuleProvider` to retrieve its own rule records from a database, configuration, or remote service, map them to `Rule`, then call `RbcEngine.check`. The library has no Spring or database dependency.

Build with `mvn clean test` or `mvn clean install`.
