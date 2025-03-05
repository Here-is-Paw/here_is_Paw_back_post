package com.ll.hereispaw.domain.search.search.service;

import com.ll.hereispaw.domain.search.search.document.PostDocument;
import com.ll.hereispaw.domain.search.search.repository.PostDocumentRepository;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final PostDocumentRepository postDocumentRepository;


    public void add(PostDocument postDoc, String indexName) {
        postDocumentRepository.save(postDoc, indexName);
    }

    public void clear(String indexName) {
        postDocumentRepository.clear(indexName);
    }

    public SearchResult typeAllSearch(String kw) {
        return postDocumentRepository.search("post", kw);
    }
}
