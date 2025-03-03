package com.ll.hereispaw.domain.search.search.repository;

import com.ll.hereispaw.domain.search.search.dto.PostDto;
import com.meilisearch.sdk.model.SearchResult;


public interface PostDocumentRepository {

    void save(PostDto postDto, String indexName);

    void clear(String indexName);

    SearchResult search(String indexName, String kw);
}
