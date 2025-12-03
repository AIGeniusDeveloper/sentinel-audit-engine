package com.sentinel.audit.infrastructure.ai.graph;

import com.sentinel.audit.application.dto.Transaction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditState extends AgentState {

    public AuditState(Map<String, Object> initData) {
        super(initData);
    }

    public Transaction getTransaction() {
        return (Transaction) this.data().get("transaction");
    }

    public String getKycStatus() {
        return (String) this.data().get("kycStatus");
    }

    public String getLimitCheck() {
        return (String) this.data().get("limitCheck");
    }

    public List<String> getRegulations() {
        return (List<String>) this.data().getOrDefault("regulations", new ArrayList<>());
    }

    public String getReport() {
        return (String) this.data().get("report");
    }
}
