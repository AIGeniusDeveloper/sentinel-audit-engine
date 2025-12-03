package com.sentinel.audit.application.port;

import java.util.List;

/**
 * Interface for regulatory information retrieval.
 * Follows Dependency Inversion Principle (SOLID).
 */
public interface RegulatoryProvider {

    /**
     * Retrieves relevant regulatory clauses based on a query.
     * 
     * @param query The search query
     * @return List of relevant regulatory texts
     */
    List<String> retrieveRelevantInfo(String query);
}
