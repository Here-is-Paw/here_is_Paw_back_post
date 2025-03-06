package com.ll.hereispaw.domain.search.search.repository;

import com.ll.hereispaw.domain.search.search.document.PostDocument;
import com.meilisearch.sdk.model.SearchResult;


public interface PostDocumentRepository {

    void save(PostDocument postDoc, String indexName);

    void clear(String indexName);

    SearchResult search(String indexName, String kw);
}
