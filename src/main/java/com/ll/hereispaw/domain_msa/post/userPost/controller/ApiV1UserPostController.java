package com.ll.hereispaw.domain_msa.post.userPost.controller;

import com.ll.hereispaw.domain_msa.post.userPost.service.UserPostService;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.globalDto.GlobalResponse;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import com.ll.hereispaw.global_msa.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1UserPostController {
    private final UserPostService userPostService;

    @GetMapping
    public GlobalResponse<?> lists(@LoginUser MemberDto loginUser) {
        if (loginUser == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        return GlobalResponse.success(userPostService.getMyWritePost(loginUser));
    }
}
