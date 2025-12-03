package com.sentinel.audit.infrastructure.persistence.jpa;

import com.sentinel.audit.domain.model.RegulatoryDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRegulatoryDocumentRepository extends JpaRepository<RegulatoryDocument, Long> {
}
