package com.example.audit.repository;
import com.example.audit.model.AuditEvent; import java.util.*; import java.util.concurrent.CopyOnWriteArrayList;
/** Thread-safe support repository useful for tests and local demonstrations, not durable persistence. */
public final class InMemoryAuditRepository implements AuditRepository { private final List<AuditEvent> events=new CopyOnWriteArrayList<>(); public void save(AuditEvent event){events.add(Objects.requireNonNull(event));} public List<AuditEvent> findAll(){return List.copyOf(events);} }
