package com.sentinel.audit.infrastructure.persistence.jpa;

import com.sentinel.audit.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuditLogRepository extends JpaRepository<AuditLog, Long> {
}
