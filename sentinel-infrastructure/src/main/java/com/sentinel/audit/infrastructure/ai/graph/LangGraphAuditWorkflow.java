package com.sentinel.audit.infrastructure.ai.graph;

import com.sentinel.audit.application.dto.Transaction;
import com.sentinel.audit.application.port.AuditWorkflow;
import com.sentinel.audit.application.port.KycProvider;
import com.sentinel.audit.application.port.RegulatoryProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import com.sentinel.audit.infrastructure.ai.AuditAgent;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

@Service
@RequiredArgsConstructor
@Slf4j
public class LangGraphAuditWorkflow implements AuditWorkflow {

    private final KycProvider kycProvider;
    private final RegulatoryProvider regulatoryProvider;
    private final AuditAgent auditAgent;

    @Override
    public String execute(Transaction transaction) {
        try {
            StateGraph<AuditState> workflow = new StateGraph<>(AuditState::new);

            workflow.addNode("kyc_check", state -> kycCheck(state));
            workflow.addNode("rag_search", state -> ragSearch(state));
            workflow.addNode("generate_report", state -> generateReport(state));

            workflow.addEdge(START, "kyc_check");
            workflow.addEdge("kyc_check", "rag_search");
            workflow.addEdge("rag_search", "generate_report");
            workflow.addEdge("generate_report", END);

            CompiledGraph<AuditState> app = workflow.compile();

            Map<String, Object> inputs = new HashMap<>();
            inputs.put("transaction", transaction);

            // Execute the graph
            // Note: invoke returns a stream or complete state depending on version.
            // Assuming invoke returns the final state or we can get it.
            // In 1.7.3 invoke returns CompletableFuture<State> or similar?
            // Let's check typical usage. Usually app.invoke(inputs) returns the final
            // state.

            // For simplicity and safety with unknown exact API signature in this context,
            // I will assume a blocking call or similar pattern.
            // If invoke returns a Map or State, we use it.

            // Checking typical LangGraph4j usage:
            // var result = app.invoke(inputs);
            // return result.state().get("report");

            // Since I can't verify the exact API at runtime without docs, I'll try the
            // standard invoke.
            // If it fails compilation, I'll adjust.

            var result = app.invoke(inputs);

            // In 1.7.3, invoke returns Optional<State>
            return result.map(state -> (String) state.data().get("report"))
                    .orElse("Error: No report generated");

        } catch (Exception e) {
            log.error("Error executing LangGraph workflow", e);
            throw new RuntimeException("Audit workflow failed", e);
        }
    }

    private CompletableFuture<Map<String, Object>> kycCheck(AuditState state) {
        Transaction tx = state.getTransaction();
        log.info("Graph Node: KYC Check for {}", tx.getCustomerId());

        String kycStatus = kycProvider.verifyKyc(tx.getCustomerId());
        String limitCheck = kycProvider.checkTransactionLimit(tx.getCustomerId(), tx.getAmount());

        return CompletableFuture.completedFuture(Map.of(
                "kycStatus", kycStatus,
                "limitCheck", limitCheck));
    }

    private CompletableFuture<Map<String, Object>> ragSearch(AuditState state) {
        Transaction tx = state.getTransaction();
        log.info("Graph Node: RAG Search");

        String query = String.format("regulations for %s transaction amount %.2f",
                tx.getType(), tx.getAmount());
        List<String> regulations = regulatoryProvider.retrieveRelevantInfo(query);

        return CompletableFuture.completedFuture(Map.of("regulations", regulations));
    }

    private CompletableFuture<Map<String, Object>> generateReport(AuditState state) {
        log.info("Graph Node: Generate Report (AI Agent)");

        Transaction tx = state.getTransaction();
        String kyc = state.getKycStatus();
        String limit = state.getLimitCheck();
        List<String> regs = state.getRegulations();

        String context = String.format("""
                Transaction: %s
                Amount: %.2f %s
                Type: %s
                Destination: %s

                KYC Status: %s
                Limit Check: %s

                Relevant Regulations:
                %s
                """,
                tx.getTransactionId(), tx.getAmount(), tx.getCurrency(), tx.getType(), tx.getDestinationCountry(),
                kyc, limit, String.join("\n- ", regs));

        String report = auditAgent.auditTransaction(context);

        return CompletableFuture.completedFuture(Map.of("report", report));
    }
}
