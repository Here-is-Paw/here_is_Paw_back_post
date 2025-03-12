package com.ll.hereispaw.domain_msa.post.userPost.service;

import com.ll.hereispaw.domain_msa.post.find.repository.FindRepository;
import com.ll.hereispaw.domain_msa.post.missing.repository.MissingRepository;
import com.ll.hereispaw.domain_msa.post.userPost.dto.response.PostListDto;
import com.ll.hereispaw.domain_msa.post.userPost.dto.response.PostListResponse;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserPostService {

    private final FindRepository findRepository;
    private final MissingRepository missingRepository;

    public PostListResponse getMyWritePost(MemberDto loginUser) {

        List<PostListDto> findingPostList = findRepository.findByMemberId(loginUser.getId())
                .stream()
                .map(PostListDto::new)
                .toList();

        List<PostListDto> missingPostList = missingRepository.findByMemberId(loginUser.getId())
                .stream()
                .map(PostListDto::new)
                .toList();

        PostListResponse listResponse = new PostListResponse();
        listResponse.setFindingList(findingPostList);
        listResponse.setMissingList(missingPostList);
        return listResponse;
    }
}
