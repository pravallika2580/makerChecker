package com.example.rbc.model;

import com.example.rbc.exception.InvalidRuleException;
import java.util.List;
import java.util.Objects;

/** An allow rule for a role, action, resource triple, optionally constrained by conditions. */
public final class Rule {
    private final String role;
    private final String action;
    private final String resource;
    private final int priority;
    private final List<Condition> conditions;

    private Rule(Builder builder) {
        this.role = required(builder.role, "role");
        this.action = required(builder.action, "action");
        this.resource = required(builder.resource, "resource");
        this.priority = builder.priority;
        this.conditions = List.copyOf(builder.conditions);
    }
    public String getRole() { return role; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public int getPriority() { return priority; }
    public List<Condition> getConditions() { return conditions; }
    public static Builder builder() { return new Builder(); }
    private static String required(String value, String name) {
        if (value == null || value.isBlank()) throw new InvalidRuleException("rule " + name + " must not be blank");
        return value;
    }
    public static final class Builder {
        private String role;
        private String action;
        private String resource;
        private int priority;
        private final java.util.ArrayList<Condition> conditions = new java.util.ArrayList<>();
        public Builder role(String role) { this.role = role; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder resource(String resource) { this.resource = resource; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder condition(Condition condition) { conditions.add(Objects.requireNonNull(condition)); return this; }
        public Builder conditions(List<Condition> conditions) { Objects.requireNonNull(conditions).forEach(this::condition); return this; }
        public Rule build() { return new Rule(this); }
    }
}
