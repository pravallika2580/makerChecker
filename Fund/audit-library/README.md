# Audit Library

Framework-independent Java 17 audit-event library. It records generic immutable events through an `AuditRepository` supplied by the consuming application. It has no Spring, database, or banking dependency.

## Use

```java
AuditRepository repository = new MySqlAuditRepository(dataSource); // application code
AuditService auditService = new AuditService(repository);

auditService.record(AuditEvent.builder()
    .actor("maker1")
    .role("MAKER1")
    .action("CREATE")
    .resource("BENEFICIARY")
    .resourceId("101")
    .status(AuditStatus.SUCCESS)
    .description("Beneficiary created")
    .metadata(Map.of("amount", "5000"))
    .build());
```

`AuditEvent` generates a UUID and `Instant` timestamp when omitted, validates required actor/action/resource/status fields, and copies metadata immutably. `AuditService` is stateless and safe to share across concurrent application requests. The consumer implements `AuditRepository.save` using any storage technology it prefers.

For local tests, `InMemoryAuditRepository` provides thread-safe non-durable storage.

Run `mvn clean test` or `mvn clean install`.
