package com.sentinel.audit.domain.repository;

import com.sentinel.audit.domain.model.AuditLog;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AuditLog (Port in Hexagonal Architecture).
 * Implementation will be in infrastructure layer.
 */
public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

    Optional<AuditLog> findById(Long id);

    List<AuditLog> findAll();
}
