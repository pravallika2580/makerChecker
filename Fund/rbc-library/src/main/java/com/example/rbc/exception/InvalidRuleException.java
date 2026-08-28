package com.example.rbc.exception;

/** Thrown when a rule or condition is structurally invalid. */
public final class InvalidRuleException extends RbcException {
    public InvalidRuleException(String message) { super(message); }
}
