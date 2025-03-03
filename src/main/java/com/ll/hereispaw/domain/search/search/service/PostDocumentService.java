package com.ll.hereispaw.domain.search.search.service;

import com.ll.hereispaw.domain.search.search.dto.PostDto;
import com.ll.hereispaw.domain.search.search.repository.PostDocumentRepository;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDocumentService {
    private final PostDocumentRepository postDocumentRepository;


    public void add(PostDto postDto) {
        postDocumentRepository.save(postDto, "post");
    }

    public void clear() {
        postDocumentRepository.clear("post");
    }

    public SearchResult typeAllSearch(String kw) {
        return postDocumentRepository.search("post", kw);
    }
}
