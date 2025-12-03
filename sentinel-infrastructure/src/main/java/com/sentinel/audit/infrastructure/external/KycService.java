package com.sentinel.audit.infrastructure.external;

import com.sentinel.audit.application.port.KycProvider;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KycService implements KycProvider {

    @Tool("Calls the external KYC Provider to retrieve the verification status of a customer")
    @Override
    public String verifyKyc(String customerId) {
        log.info("Calling external KYC provider for customer: {}", customerId);

        // Simulate external API call
        String externalStatus = callExternalKycProvider(customerId);

        return String.format("KYC Provider Response: %s", externalStatus);
    }

    private String callExternalKycProvider(String customerId) {
        // Mocking an HTTP call to a provider like Onfido or SumSub
        if (customerId.startsWith("HIGH_RISK")) {
            return "{ \"status\": \"FLAGGED\", \"reason\": \"PEP_MATCH\", \"provider\": \"Onfido\" }";
        } else if (customerId.startsWith("UNKNOWN")) {
            return "{ \"status\": \"PENDING\", \"reason\": \"DOCS_MISSING\", \"provider\": \"Onfido\" }";
        } else {
            return "{ \"status\": \"VERIFIED\", \"reason\": \"CLEAR\", \"provider\": \"Onfido\" }";
        }
    }

    @Tool("Checks if a transaction amount is within the allowed limit for the customer tier")
    @Override
    public String checkTransactionLimit(String customerId, double amount) {
        log.info("Checking transaction limit for customer: {} amount: {}", customerId, amount);
        if (amount > 10000) {
            return "Limit Check: EXCEEDED. Requires enhanced due diligence.";
        }
        return "Limit Check: APPROVED.";
    }
}
