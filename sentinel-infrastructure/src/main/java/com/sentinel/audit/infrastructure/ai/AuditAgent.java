package com.sentinel.audit.infrastructure.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditAgent {

    private final ChatLanguageModel chatLanguageModel;

    private static final String SYSTEM_PROMPT = """
            You are an expert Compliance Audit Agent.
            Your task is to synthesize information from a Regulatory Search (RAG) and a Business Verification (KYC/Transaction check).

            You will receive:
            1. Relevant Regulatory Clauses.
            2. Business Verification Results.
            3. Transaction Details.

            You must:
            - Analyze if the transaction is compliant based on the regulations and the verification results.
            - Generate a clear, traceable Audit Report.
            - Cite the specific regulatory clauses used.
            - Conclude with a FINAL VERDICT: COMPLIANT or NON-COMPLIANT.
            """;

    public String auditTransaction(String context) {
        String fullPrompt = SYSTEM_PROMPT + "\n\nContext:\n" + context;
        return chatLanguageModel.generate(fullPrompt);
    }
}
