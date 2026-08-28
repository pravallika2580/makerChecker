package com.example.audit.repository;
import com.example.audit.model.AuditEvent;
/** Persistence port implemented by the consuming application. */
@FunctionalInterface public interface AuditRepository { void save(AuditEvent event); }
