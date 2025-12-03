package com.sentinel.audit.infrastructure.external;

import com.sentinel.audit.domain.model.RegulatoryCategory;
import com.sentinel.audit.domain.model.RegulatoryDocument;
import com.sentinel.audit.domain.repository.RegulatoryDocumentRepository;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RagServiceTest {

    @Mock
    private RegulatoryDocumentRepository repository;

    @Mock
    private EmbeddingModel embeddingModel;

    @Mock
    private EmbeddingStore<TextSegment> embeddingStore;

    @InjectMocks
    private RagService ragService;

    @Test
    void shouldIngestDocumentSuccessfully() {
        // Given
        String content = "Article 12: High value transactions require EDD";
        String source = "AML Directive";
        RegulatoryCategory category = RegulatoryCategory.AML;

        when(repository.save(any(RegulatoryDocument.class))).thenAnswer(i -> i.getArgument(0));
        when(embeddingModel.embed(any(TextSegment.class)))
                .thenReturn(mock(dev.langchain4j.model.output.Response.class));

        // When
        ragService.ingestDocument(content, source, category);

        // Then
        verify(repository, times(1)).save(any(RegulatoryDocument.class));
        verify(embeddingStore, times(1)).add(any(), any(TextSegment.class));
    }

    @Test
    void shouldRetrieveRelevantRegulations() {
        // Given
        String query = "high value transaction regulations";
        TextSegment segment = TextSegment.from("Article 12: EDD required");
        EmbeddingMatch<TextSegment> match = new EmbeddingMatch<>(0.9, "id", null, segment);

        when(embeddingModel.embed(query)).thenReturn(mock(dev.langchain4j.model.output.Response.class));
        when(embeddingStore.findRelevant(any(), anyInt(), anyDouble()))
                .thenReturn(List.of(match));

        // When
        List<String> results = ragService.retrieveRelevantInfo(query);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0)).contains("Article 12");
    }
}
