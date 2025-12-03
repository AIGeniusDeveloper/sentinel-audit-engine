package com.sentinel.audit.infrastructure.config;

import com.sentinel.audit.infrastructure.external.RagService;
import com.sentinel.audit.domain.model.RegulatoryCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

        private final RagService ragService;

        @Override
        public void run(String... args) throws Exception {
                // Seed some dummy regulations
                ragService.ingestDocument(
                                "Article 12: High value transactions (over 10,000 EUR) require Enhanced Due Diligence (EDD).",
                                "AML Directive 2024",
                                RegulatoryCategory.AML);

                ragService.ingestDocument(
                                "Section 5: Customers on the Watch List must be flagged immediately and transactions blocked.",
                                "Global Sanctions Policy",
                                RegulatoryCategory.KYC);

                ragService.ingestDocument(
                                "Rule 3: All cross-border transactions must declare the beneficiary owner.",
                                "Wire Transfer Regulation",
                                RegulatoryCategory.COMPLIANCE);
        }
}
