package com.example.audit.exception;
/** Wraps a repository failure without tying the caller to a persistence technology. */
public final class AuditPersistenceException extends AuditException { public AuditPersistenceException(String message,Throwable cause){super(message,cause);} }
