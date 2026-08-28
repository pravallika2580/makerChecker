package com.example.audit.service;
import com.example.audit.model.AuditEvent; import com.example.audit.repository.AuditRepository; import com.example.audit.exception.*; import java.util.Objects;
/** Stateless, thread-safe facade which validates event creation and delegates durable storage. */
public final class AuditService { private final AuditRepository repository; public AuditService(AuditRepository repository){this.repository=Objects.requireNonNull(repository,"repository must not be null");} public void record(AuditEvent event){if(event==null)throw new InvalidAuditEventException("event must not be null");try{repository.save(event);}catch(AuditException e){throw e;}catch(RuntimeException e){throw new AuditPersistenceException("Unable to persist audit event "+event.getId(),e);}} }
