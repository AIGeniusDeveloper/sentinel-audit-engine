package com.sentinel.audit.infrastructure.external;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KycServiceTest {

    @InjectMocks
    private KycService kycService;

    @Test
    void shouldFlagHighRiskCustomer() {
        // Given
        String customerId = "HIGH_RISK_USER_001";

        // When
        String result = kycService.verifyKyc(customerId);

        // Then
        assertThat(result).contains("FLAGGED");
        assertThat(result).contains("PEP_MATCH");
    }

    @Test
    void shouldVerifyNormalCustomer() {
        // Given
        String customerId = "NORMAL_USER_001";

        // When
        String result = kycService.verifyKyc(customerId);

        // Then
        assertThat(result).contains("VERIFIED");
        assertThat(result).contains("CLEAR");
    }

    @Test
    void shouldDetectPendingKyc() {
        // Given
        String customerId = "UNKNOWN_USER_001";

        // When
        String result = kycService.verifyKyc(customerId);

        // Then
        assertThat(result).contains("PENDING");
        assertThat(result).contains("DOCS_MISSING");
    }

    @Test
    void shouldApproveSmallTransaction() {
        // Given
        String customerId = "USER_001";
        double amount = 5000.0;

        // When
        String result = kycService.checkTransactionLimit(customerId, amount);

        // Then
        assertThat(result).contains("APPROVED");
    }

    @Test
    void shouldRejectLargeTransaction() {
        // Given
        String customerId = "USER_001";
        double amount = 15000.0;

        // When
        String result = kycService.checkTransactionLimit(customerId, amount);

        // Then
        assertThat(result).contains("EXCEEDED");
    }
}
