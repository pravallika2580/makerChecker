package com.example.rbc.model;

import java.util.Objects;

/** Result of an access evaluation, including a diagnostic reason. */
public final class AccessDecision {
    private final boolean allowed;
    private final String reason;

    private AccessDecision(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = Objects.requireNonNull(reason);
    }

    public static AccessDecision allowed(String reason) {
        return new AccessDecision(true, reason);
    }

    public static AccessDecision denied(String reason) {
        return new AccessDecision(false, reason);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getReason() {
        return reason;
    }
}
