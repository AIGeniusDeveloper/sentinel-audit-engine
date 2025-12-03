package com.sentinel.audit.domain.model;

public enum TransactionType {
    WIRE_TRANSFER,
    CREDIT_CARD,
    ACH,
    CRYPTO_TRANSFER,
    SEPA,
    SWIFT,
    OTHER
}
