package com.example.rbc.engine;

import com.example.rbc.model.AccessDecision;
import com.example.rbc.model.AccessRequest;
import com.example.rbc.model.Rule;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Evaluates supplied rules; it has no persistence or framework responsibility. */
public final class RbcEngine {
    public AccessDecision check(AccessRequest request, List<Rule> rules) {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(rules, "rules must not be null");
        List<Rule> applicable = rules.stream().filter(Objects::nonNull).filter(rule ->
                rule.getRole().equals(request.getRole()) && rule.getAction().equals(request.getAction())
                        && rule.getResource().equals(request.getResource())).toList();
        if (applicable.isEmpty()) return AccessDecision.denied("No matching rule for role, action, and resource");

        int highestPriority = applicable.stream().map(Rule::getPriority).max(Comparator.naturalOrder()).orElseThrow();
        List<Rule> governing = applicable.stream().filter(rule -> rule.getPriority() == highestPriority).toList();
        boolean granted = governing.stream().anyMatch(rule -> rule.getConditions().stream()
                .allMatch(condition -> condition.matches(request.getAttribute(condition.getAttribute()))));
        return granted
                ? AccessDecision.allowed("Access allowed by matching rule at priority " + highestPriority)
                : AccessDecision.denied("No rule at highest matching priority satisfies all conditions");
    }
}
