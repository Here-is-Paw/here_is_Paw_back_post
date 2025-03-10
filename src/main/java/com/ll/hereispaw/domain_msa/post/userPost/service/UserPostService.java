package com.ll.hereispaw.domain_msa.post.userPost.service;

import com.ll.hereispaw.domain_msa.post.find.repository.FindRepository;
import com.ll.hereispaw.domain_msa.post.missing.dto.response.MissingListResponse;
import com.ll.hereispaw.domain_msa.post.missing.repository.MissingRepository;
import com.ll.hereispaw.domain_msa.post.userPost.dto.response.PostListResponse;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserPostService {

    private final FindRepository findRepository;
    private final MissingRepository missingRepository;

    public List<PostListResponse> getMyWritePost(MemberDto loginUser) {
        List<PostListResponse> postList;

        findRepository.findByMemberId(loginUser.getId());
        missingRepository.findByMemberId(loginUser.getId());

        return null;
//        return PostListResponse.map(MissingListResponse::new)
    }
}
