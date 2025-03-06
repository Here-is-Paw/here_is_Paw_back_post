package com.ll.hereispaw.global_msa.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogFaceRequest {
  private String type;
  private String image;
  private String postType;
  private Long postId;
  private Long postMemberId;
}