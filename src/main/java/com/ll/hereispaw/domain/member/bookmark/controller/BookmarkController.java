package com.ll.hereispaw.domain.member.bookmark.controller;


import com.ll.hereispaw.domain.member.bookmark.dto.response.BookmarkDto;
import com.ll.hereispaw.domain.member.bookmark.service.BookmarkService;
import com.ll.hereispaw.domain.member.member.entity.Member;
import com.ll.hereispaw.global.error.ErrorCode;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import com.ll.hereispaw.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 실종 북마크 추가
    @PostMapping("/missing/{postId}")
    public GlobalResponse<String> addMissingBookmark(@LoginUser Member loginUser, @PathVariable Long postId) {
        if (loginUser == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        bookmarkService.addMissingBookmark(loginUser, postId);
        return GlobalResponse.createSuccess("북마크 추가 완료");
    }

    // 발견 북마크 추가
    @PostMapping("/find/{postId}")
    public String addFindBookmark(@LoginUser Member loginUser, @PathVariable Long postId) {
        return "북마크 추가";
    }

    // 북마크 조회
    @GetMapping("")
    public GlobalResponse<?> getBookmarks(@LoginUser Member loginUser) {
        if (loginUser == null) {
            return GlobalResponse.error(ErrorCode.ACCESS_DENIED);
        }

        List<BookmarkDto> bookmarks = bookmarkService.getBookmarks(loginUser);
        return GlobalResponse.success(bookmarks);
    }

    // 북마크 삭제
    @DeleteMapping("/{bookmarkId}")
    public GlobalResponse<String> deleteBookmark(@LoginUser Member loginUser, @PathVariable Long bookmarkId) {
        bookmarkService.removeBookmark(bookmarkId);
        return GlobalResponse.success("삭제 성공");
    }
}