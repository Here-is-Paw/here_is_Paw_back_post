package com.ll.hereispaw.domain_msa.post.userPost.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PostListResponse {
    List<PostListDto> findingList;
    List<PostListDto> missingList;
}
