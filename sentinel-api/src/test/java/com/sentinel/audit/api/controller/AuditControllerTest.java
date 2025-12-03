package com.sentinel.audit.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.audit.domain.model.TransactionType;
import com.sentinel.audit.application.dto.Transaction;
import com.sentinel.audit.application.usecase.AuditTransactionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuditController.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditTransactionUseCase auditTransactionUseCase;

    @Test
    void shouldAuditValidTransaction() throws Exception {
        // Given
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN-001");
        transaction.setCustomerId("CUST-001");
        transaction.setAmount(5000.0);
        transaction.setCurrency("EUR");
        transaction.setType(TransactionType.WIRE_TRANSFER);
        transaction.setDestinationCountry("France");

        String expectedReport = "COMPLIANT";
        when(auditTransactionUseCase.execute(any())).thenReturn(expectedReport);

        // When & Then
        mockMvc.perform(post("/api/audit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedReport));
    }

    @Test
    void shouldRejectInvalidTransaction_MissingCustomerId() throws Exception {
        // Given
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN-001");
        // customerId is missing
        transaction.setAmount(5000.0);
        transaction.setCurrency("EUR");
        transaction.setType(TransactionType.WIRE_TRANSFER);
        transaction.setDestinationCountry("France");

        // When & Then
        mockMvc.perform(post("/api/audit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void shouldRejectInvalidTransaction_NegativeAmount() throws Exception {
        // Given
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN-001");
        transaction.setCustomerId("CUST-001");
        transaction.setAmount(-100.0); // Invalid
        transaction.setCurrency("EUR");
        transaction.setType(TransactionType.WIRE_TRANSFER);
        transaction.setDestinationCountry("France");

        // When & Then
        mockMvc.perform(post("/api/audit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].field").value("amount"));
    }
}
