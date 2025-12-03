package com.sentinel.audit.application.usecase;

import com.sentinel.audit.application.dto.Transaction;
import com.sentinel.audit.application.port.AuditWorkflow;
import com.sentinel.audit.domain.model.AuditLog;
import com.sentinel.audit.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditTransactionUseCase {

    private final AuditWorkflow auditWorkflow;
    private final AuditLogRepository auditLogRepository;

    public String execute(Transaction transaction) {
        log.info("Executing audit for transaction: {}", transaction.getTransactionId());

        // Delegate to LangGraph Workflow
        String report = auditWorkflow.execute(transaction);

        // Save audit log
        AuditLog auditLog = new AuditLog();
        auditLog.setTransactionId(transaction.getTransactionId());
        auditLog.setReport(report);
        auditLog.setTimestamp(java.time.LocalDateTime.now());
        auditLogRepository.save(auditLog);

        return report;
    }
}
