package com.ll.hereispaw.domain.member.member.dto.response;


import com.ll.hereispaw.domain.member.member.entity.Member;
import jakarta.validation.constraints.NotNull;

public record MemberInfoDto(@NotNull Long id, @NotNull String username, @NotNull String nickname, String avatar) {
    public MemberInfoDto(Member member) {
        this(member.getId(), member.getUsername(), member.getNickname(), member.getAvatar());
    }
}