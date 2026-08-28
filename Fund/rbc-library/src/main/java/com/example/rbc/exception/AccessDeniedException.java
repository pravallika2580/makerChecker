package com.example.rbc.exception;

/** Optional convenience exception for consumers that prefer exception-based denial handling. */
public final class AccessDeniedException extends RbcException {
    public AccessDeniedException(String message) { super(message); }
}
