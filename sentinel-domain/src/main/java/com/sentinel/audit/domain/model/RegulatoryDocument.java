package com.sentinel.audit.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "regulatory_documents")
@Data
public class RegulatoryDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String source;

    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    private RegulatoryCategory category; // e.g., GDPR, AML, KYC
}
