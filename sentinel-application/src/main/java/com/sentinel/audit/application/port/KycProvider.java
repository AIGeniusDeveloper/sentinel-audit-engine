package com.sentinel.audit.application.port;

/**
 * Interface for KYC verification services.
 * Follows Dependency Inversion Principle (SOLID).
 */
public interface KycProvider {

    /**
     * Verifies the KYC status of a customer.
     * 
     * @param customerId The customer identifier
     * @return KYC verification result
     */
    String verifyKyc(String customerId);

    /**
     * Checks if a transaction amount is within allowed limits.
     * 
     * @param customerId The customer identifier
     * @param amount     The transaction amount
     * @return Limit check result
     */
    String checkTransactionLimit(String customerId, double amount);
}
