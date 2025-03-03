package com.ll.hereispaw.domain.search.search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hereispaw.domain.search.search.dto.PostDto;
import com.ll.hereispaw.domain.search.search.service.PostDocumentService;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class PostController {
    private final PostDocumentService postDocumentService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("add")
    public GlobalResponse<String> add(PostDto postDto) {
        postDocumentService.add(postDto);
        return GlobalResponse.createSuccess("메일리서치 저장 완료");
    }

    @GetMapping
    public GlobalResponse<SearchResult> search(@RequestParam("kw") String kw) {
        SearchResult result = postDocumentService.typeAllSearch(kw);

        return GlobalResponse.success(result);
    }

}
