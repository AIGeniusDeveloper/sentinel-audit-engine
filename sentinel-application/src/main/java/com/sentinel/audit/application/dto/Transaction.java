package com.sentinel.audit.application.dto;

import com.sentinel.audit.domain.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Transaction {
    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotBlank(message = "Destination country is required")
    private String destinationCountry;
}
