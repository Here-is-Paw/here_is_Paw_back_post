package com.ll.hereispaw.domain_msa.missing.missing.controller;

import com.ll.hereispaw.domain_msa.missing.missing.dto.request.MissingRequest;
import com.ll.hereispaw.domain_msa.missing.missing.dto.response.MissingResponse;
import com.ll.hereispaw.domain_msa.missing.missing.service.MissingService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public GlobalResponse<Page<MissingResponse>> lists(
        @PageableDefault(size = 10) Pageable pageable) {

        return GlobalResponse.success(missingService.list(pageable));
    }

    // 작성
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public GlobalResponse<MissingResponse> write(
            @LoginUser MemberDto author,
            @Valid @ModelAttribute MissingRequest missingRequest) {
        return GlobalResponse.success(missingService.write(author, missingRequest));
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
            @Valid @ModelAttribute MissingRequest missingRequest,
            @PathVariable("missingId") Long missingId) {

        if (author == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        return GlobalResponse.success(missingService.update(author, missingRequest, missingId));
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
