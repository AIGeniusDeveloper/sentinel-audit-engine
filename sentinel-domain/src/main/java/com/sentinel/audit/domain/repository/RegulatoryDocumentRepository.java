package com.sentinel.audit.domain.repository;

import com.sentinel.audit.domain.model.RegulatoryDocument;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RegulatoryDocument (Port in Hexagonal Architecture).
 * Implementation will be in infrastructure layer.
 */
public interface RegulatoryDocumentRepository {

    RegulatoryDocument save(RegulatoryDocument document);

    Optional<RegulatoryDocument> findById(Long id);

    List<RegulatoryDocument> findAll();
}
