package com.ll.hereispaw.domain.member.member.controller;

import com.ll.hereispaw.domain.member.member.dto.request.LoginRequest;
import com.ll.hereispaw.domain.member.member.dto.request.SignupRequest;
import com.ll.hereispaw.domain.member.member.dto.request.ModifyRequest;
import com.ll.hereispaw.domain.member.member.dto.response.LoginResponse;
import com.ll.hereispaw.domain.member.member.dto.response.MemberInfoDto;
import com.ll.hereispaw.domain.member.member.entity.Member;
import com.ll.hereispaw.domain.member.member.service.MemberService;
import com.ll.hereispaw.global.rq.Rq;
import com.ll.hereispaw.global.webMvc.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ApiV1MemberController {

    private final MemberService memberService;
    private final Rq rq;

    // 유저 기본 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity<?> me(@LoginUser Member loginUser) {

        if (loginUser == null) {
            // 미로그인시 401 Unauthorized 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인을 하지 않은 유저");
        }

        MemberInfoDto userInfo = memberService.me(loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    // 어드민 - 유저 추가
    // username (= id)의 앞에 admin 입력하여 등록 시 admin 권한 부여
    // username (= id)의 앞에 manager 입력하여 등록 시 manager 권한 부여
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRq) {
        memberService.signup(signupRq);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원 생성 완료");
    }

    // 어드민 - 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRq) {
        LoginResponse loginUser = memberService.login(loginRq);

        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }

    @PatchMapping("/radius-update")
    public ResponseEntity<String> radius_update(@LoginUser Member loginUser, Integer radius) {
        memberService.radius_update(loginUser, radius);
        return ResponseEntity.ok("로케이션 업데이트");
    }

    @PatchMapping("/modify")
    public ResponseEntity<String> modify(@LoginUser Member member, ModifyRequest modifyRequest) {
        memberService.modify(member, modifyRequest);

        return ResponseEntity.ok("수정 완료");
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("apiKey");

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다.");
    }
}
