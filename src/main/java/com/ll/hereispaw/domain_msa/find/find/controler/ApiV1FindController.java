package com.ll.hereispaw.domain_msa.find.find.controler;

import com.ll.hereispaw.domain_msa.find.find.dto.request.FindRequest;
import com.ll.hereispaw.domain_msa.find.find.dto.response.FindResponse;
import com.ll.hereispaw.domain_msa.find.find.service.FindService;
import com.ll.hereispaw.global_msa.globalDto.GlobalResponse;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import com.ll.hereispaw.global_msa.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/finding")
@RequiredArgsConstructor
public class ApiV1FindController {

    private final FindService findService;


    // 유기견 발견 전체 조회
    @GetMapping
    public GlobalResponse<Page<FindResponse>> lists(
        @PageableDefault(size = 10) Pageable pageable) {

        return GlobalResponse.success(findService.list(pageable));
    }


    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GlobalResponse<FindResponse> writeFindPost(
        @LoginUser MemberDto author,
        @ModelAttribute FindRequest request,  // Point 필드가 없는 DTO 사용
        @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponse.success(findService.write(author, request, file));
    }

    // 발견 단건 조회
    @GetMapping("/{findingId}")
    public GlobalResponse<FindResponse> findPostById(
        @PathVariable("findingId") Long findingId) {
        return GlobalResponse.success(findService.findById(findingId));
    }

    @PutMapping("/{postId}")
    public GlobalResponse<FindResponse> update(
            @LoginUser MemberDto author,
            @PathVariable("postId") Long findingId,
            @ModelAttribute FindRequest request,  // Point 필드가 없는 DTO 사용
            @RequestPart("file") MultipartFile file) {

        return GlobalResponse.success(findService.update(author, request, findingId, file));
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
