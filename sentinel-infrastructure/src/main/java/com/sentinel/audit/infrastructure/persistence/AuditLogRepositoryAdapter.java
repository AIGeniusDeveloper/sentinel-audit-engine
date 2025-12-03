package com.sentinel.audit.infrastructure.persistence;

import com.sentinel.audit.domain.model.AuditLog;
import com.sentinel.audit.domain.repository.AuditLogRepository;
import com.sentinel.audit.infrastructure.persistence.jpa.JpaAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepository {

    private final JpaAuditLogRepository jpaRepository;

    @Override
    public AuditLog save(AuditLog auditLog) {
        return jpaRepository.save(auditLog);
    }

    @Override
    public Optional<AuditLog> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<AuditLog> findAll() {
        return jpaRepository.findAll();
    }
}
