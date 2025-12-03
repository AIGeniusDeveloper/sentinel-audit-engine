package com.sentinel.audit.infrastructure.external;

import com.sentinel.audit.domain.model.RegulatoryDocument;
import com.sentinel.audit.domain.model.RegulatoryCategory;
import com.sentinel.audit.domain.repository.RegulatoryDocumentRepository;
import com.sentinel.audit.application.port.RegulatoryProvider;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagService implements RegulatoryProvider {

    private final RegulatoryDocumentRepository repository;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public void ingestDocument(String content, String source, RegulatoryCategory category) {
        log.info("Ingesting document from source: {}", source);

        // Save to DB
        RegulatoryDocument doc = new RegulatoryDocument();
        doc.setContent(content);
        doc.setSource(source);
        doc.setCategory(category);
        repository.save(doc);

        // Embed and store in Vector DB
        Metadata metadata = Metadata.from("source", source).add("category", category.name());
        Document document = Document.from(content, metadata);
        // Simple splitting by newlines for now, can be improved with DocumentSplitter
        TextSegment segment = TextSegment.from(content, metadata);

        embeddingStore.add(embeddingModel.embed(segment).content(), segment);
        log.info("Document ingested successfully.");
    }

    @Tool("Retrieves relevant regulatory clauses based on a query")
    @Override
    public List<String> retrieveRelevantInfo(String query) {
        log.info("Retrieving info for query: {}", query);
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(
                embeddingModel.embed(query).content(),
                3, // maxResults
                0.7 // minScore
        );

        return matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.toList());
    }
}
