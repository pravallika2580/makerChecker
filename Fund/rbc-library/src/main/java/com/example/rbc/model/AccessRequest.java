package com.example.rbc.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Immutable request presented to the authorization engine. */
public final class AccessRequest {
    private final String role;
    private final String action;
    private final String resource;
    private final Map<String, Object> attributes;

    private AccessRequest(Builder builder) {
        this.role = required(builder.role, "role");
        this.action = required(builder.action, "action");
        this.resource = required(builder.resource, "resource");
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(builder.attributes));
    }

    public String getRole() { return role; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public Map<String, Object> getAttributes() { return attributes; }
    public Object getAttribute(String name) { return attributes.get(name); }
    public static Builder builder() { return new Builder(); }

    private static String required(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return value;
    }

    public static final class Builder {
        private String role;
        private String action;
        private String resource;
        private final Map<String, Object> attributes = new LinkedHashMap<>();
        public Builder role(String role) { this.role = role; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder resource(String resource) { this.resource = resource; return this; }
        public Builder attribute(String name, Object value) {
            attributes.put(required(name, "attribute name"), Objects.requireNonNull(value, "attribute value must not be null"));
            return this;
        }
        public Builder attributes(Map<String, ?> values) {
            Objects.requireNonNull(values, "attributes must not be null").forEach(this::attribute);
            return this;
        }
        public AccessRequest build() { return new AccessRequest(this); }
    }
}
