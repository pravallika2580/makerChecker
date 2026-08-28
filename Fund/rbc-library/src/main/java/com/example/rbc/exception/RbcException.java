package com.example.rbc.exception;

/** Base runtime exception for malformed RBC inputs or evaluation failures. */
public class RbcException extends RuntimeException {
    public RbcException(String message) { super(message); }
    public RbcException(String message, Throwable cause) { super(message, cause); }
}
