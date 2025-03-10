package com.ll.hereispaw.domain_msa.post.missing.controller;

import com.ll.hereispaw.domain_msa.post.missing.dto.request.MissingCreateRequest;
import com.ll.hereispaw.domain_msa.post.missing.dto.request.MissingPatchRequest;
import com.ll.hereispaw.domain_msa.post.missing.dto.response.MissingListResponse;
import com.ll.hereispaw.domain_msa.post.missing.dto.response.MissingResponse;
import com.ll.hereispaw.domain_msa.post.missing.service.MissingService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/missings")
public class ApiV1MissingController {
    private final MissingService missingService;

//    // 전체 조회
//    @GetMapping
//    public GlobalResponse<List<MissingResponseDto>> lists() {
//        return GlobalResponse.success(missingService.list());
//    }
//
    // 전체 조회 페이지 적용
    @GetMapping
    public GlobalResponse<Page<MissingListResponse>> lists(
        @PageableDefault(size = 10) Pageable pageable) {

        return GlobalResponse.success(missingService.list(pageable));
    }

    @GetMapping("/radius")
    public GlobalResponse<List<MissingListResponse>> radiusList(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double radius,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String category
    ) {
        return GlobalResponse.success(missingService.radiusList(lat, lng, radius, keyword, category));
    }


    // 작성
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public GlobalResponse<MissingResponse> write(
            @LoginUser MemberDto author,
            @Valid @ModelAttribute MissingCreateRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        if (author == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        return GlobalResponse.success(missingService.write(author, request, file));
    }

    // 단건 조회
    @GetMapping("/{missingId}")
    public GlobalResponse<MissingResponse> detail(
            @PathVariable("missingId") Long missingId) {
        return GlobalResponse.success(missingService.findById(missingId));
    }

    // 수정
    @PatchMapping("/{missingId}")
    public GlobalResponse<MissingResponse> update(
            @LoginUser MemberDto author,
            @Valid @ModelAttribute MissingPatchRequest request,
            @PathVariable("missingId") Long missingId) {

        if (author == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        return GlobalResponse.success(missingService.update(author, request, missingId));
    }

    // 삭제
    @DeleteMapping("/{missingId}")
    public GlobalResponse<String> delete(
            @LoginUser MemberDto author,
            @PathVariable("missingId") Long missingId) {
        missingService.delete(author, missingId);

        return GlobalResponse.success("신고글 삭제했습니다.");
    }
}
