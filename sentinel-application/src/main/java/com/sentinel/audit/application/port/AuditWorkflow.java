package com.sentinel.audit.application.port;

import com.sentinel.audit.application.dto.Transaction;

public interface AuditWorkflow {
    String execute(Transaction transaction);
}
