package com.example.audit.exception;
/** Base exception for the framework-independent audit library. */
public class AuditException extends RuntimeException { public AuditException(String message){super(message);} public AuditException(String message,Throwable cause){super(message,cause);} }
