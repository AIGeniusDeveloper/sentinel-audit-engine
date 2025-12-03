package com.sentinel.audit.infrastructure.persistence;

import com.sentinel.audit.domain.model.RegulatoryDocument;
import com.sentinel.audit.domain.repository.RegulatoryDocumentRepository;
import com.sentinel.audit.infrastructure.persistence.jpa.JpaRegulatoryDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegulatoryDocumentRepositoryAdapter implements RegulatoryDocumentRepository {

    private final JpaRegulatoryDocumentRepository jpaRepository;

    @Override
    public RegulatoryDocument save(RegulatoryDocument document) {
        return jpaRepository.save(document);
    }

    @Override
    public Optional<RegulatoryDocument> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<RegulatoryDocument> findAll() {
        return jpaRepository.findAll();
    }
}
