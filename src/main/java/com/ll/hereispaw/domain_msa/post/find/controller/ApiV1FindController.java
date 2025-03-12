package com.ll.hereispaw.domain_msa.post.find.controller;

import com.ll.hereispaw.domain_msa.post.find.dto.request.FindCreateRequest;
import com.ll.hereispaw.domain_msa.post.find.dto.request.FindPatchRequest;
import com.ll.hereispaw.domain_msa.post.find.dto.response.FindListResponse;
import com.ll.hereispaw.domain_msa.post.find.dto.response.FindResponse;
import com.ll.hereispaw.domain_msa.post.find.service.FindService;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.globalDto.GlobalResponse;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import com.ll.hereispaw.global_msa.webMvc.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finding")
@RequiredArgsConstructor
public class ApiV1FindController {

    private final FindService findService;

    // 유기견 발견 전체 조회
    @GetMapping
    public GlobalResponse<Page<FindListResponse>> lists(
        @PageableDefault(size = 10) Pageable pageable) {

        return GlobalResponse.success(findService.list(pageable));
    }

    @GetMapping("/radius")
    public GlobalResponse<List<FindListResponse>> radiusList(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double radius,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String category
    ) {
        return GlobalResponse.success(findService.radiusList(lat, lng, radius, keyword, category));
    }

    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GlobalResponse<FindResponse> writeFindPost(
            @LoginUser MemberDto author,
            @Valid @ModelAttribute FindCreateRequest request,  // Point 필드가 없는 DTO 사용
            @RequestPart("file") MultipartFile file
    ) {
        if (author == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        return GlobalResponse.success(findService.write(author, request, file));
    }

    // 발견 단건 조회
    @GetMapping("/{postId}")
    public GlobalResponse<FindResponse> findPostById(
        @PathVariable("postId") Long findingId) {
        return GlobalResponse.success(findService.findById(findingId));
    }

    @PutMapping("/{postId}")
    public GlobalResponse<FindResponse> update(
            @LoginUser MemberDto author,
            @PathVariable("postId") Long findingId,
            @ModelAttribute FindPatchRequest request) {

        return GlobalResponse.success(findService.update(author, request, findingId));
    }

    // 발견 신고 삭제
    @DeleteMapping("/{postId}")
    public GlobalResponse<String> delete(
        @LoginUser MemberDto author,
        @PathVariable("postId") Long postId) {
        findService.delete(author, postId);
        return GlobalResponse.success("발견 신고글 삭제했습니다.");
    }
}
