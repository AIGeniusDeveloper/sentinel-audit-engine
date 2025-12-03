package com.sentinel.audit.api.controller;

import com.sentinel.audit.application.dto.Transaction;
import com.sentinel.audit.application.usecase.AuditTransactionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditTransactionUseCase auditTransactionUseCase;

    @PostMapping
    public ResponseEntity<String> auditTransaction(@Valid @RequestBody Transaction transaction) {
        String report = auditTransactionUseCase.execute(transaction);
        return ResponseEntity.ok(report);
    }
}
