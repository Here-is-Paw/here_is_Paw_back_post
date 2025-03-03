package com.ll.hereispaw.domain.missing.missing.controller;

import com.ll.hereispaw.domain.missing.Auhtor.entity.Author;
import com.ll.hereispaw.domain.missing.missing.dto.request.MissingRequestDTO;
import com.ll.hereispaw.domain.missing.missing.dto.response.MissingResponseDto;
import com.ll.hereispaw.domain.missing.missing.service.MissingService;
import com.ll.hereispaw.global.error.ErrorCode;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import com.ll.hereispaw.global.webMvc.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/missings")
public class ApiV1MissingController {
    private final MissingService missingService;

    // 전체 조회
    @GetMapping
    public GlobalResponse<List<MissingResponseDto>> lists() {
        return GlobalResponse.success(missingService.list());
    }

    // 작성
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public GlobalResponse<MissingResponseDto> write(
            @LoginUser Author author,
            @Valid @ModelAttribute MissingRequestDTO missingRequestDto) {
        return GlobalResponse.success(missingService.write(author, missingRequestDto));
    }

    // 단건 조회
    @GetMapping("/{missingId}")
    public GlobalResponse<MissingResponseDto> detail(
            @PathVariable("missingId") Long missingId) {
        return GlobalResponse.success(missingService.findById(missingId));
    }

    // 수정
    @PatchMapping("/{missingId}")
    public GlobalResponse<MissingResponseDto> update(
            @LoginUser Author author,
            @Valid @ModelAttribute MissingRequestDTO missingRequestDto,
            @PathVariable("missingId") Long missingId) {

        if (author == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        return GlobalResponse.success(missingService.update(author, missingRequestDto, missingId));
    }

    // 삭제
    @DeleteMapping("/{missingId}")
    public String delete(
            @LoginUser Author author,
            @PathVariable("missingId") Long missingId) {
        missingService.delete(author, missingId);

        return "신고글 삭제했다.";
    }
}
