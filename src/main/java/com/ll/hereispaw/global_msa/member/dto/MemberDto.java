package com.ll.hereispaw.global_msa.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
  private Long id;
  private String username;
  private String nickname;
  private String avatar;
}