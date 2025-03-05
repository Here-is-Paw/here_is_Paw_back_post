package com.ll.hereispaw.domain.search.search.repository;

import com.ll.hereispaw.domain.search.search.document.PostDocument;
import com.ll.hereispaw.global.config.MeilisearchConfig;
import com.ll.hereispaw.standard.Ut.Ut;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostDocumentRepositoryImpl implements PostDocumentRepository {
    private final MeilisearchConfig meilisearchConfig;
    private Index index;

    private Index getIndex(String indexName) {
        if (index == null) index = meilisearchConfig.meilisearchClient().index(indexName);

        return index;
    }

    @Override
    public void save(PostDocument postDoc, String indexName) {
        getIndex(indexName).addDocuments(
                Ut.json.toString(postDoc)
        );
    }

    @Override
    public void clear(String indexName) {
        getIndex(indexName).deleteAllDocuments();
    }

    @Override
    public SearchResult search(String indexName, String kw) {
        return getIndex(indexName).search(kw);
    }

}
