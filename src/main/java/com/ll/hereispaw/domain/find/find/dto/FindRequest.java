package com.ll.hereispaw.domain.find.find.dto;

import lombok.*;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindRequest {
    private Long id;

    private String title; // 제목
    private String situation; // 발견 상황
    private String breed; // 견종
    private String location; // 지역
    private String name; // 이름
    private String color; // 색상
    private String etc; // 기타 특징

    private double x;
    private double y;

    private int gender; // 성별
    private int serial_number; // 등록 번호
    private int age; // 나이
    private int neutered; // 중성화 유무


    private LocalDateTime find_date; // 발견 시간

    private Long member_id; // 신고한 회원 id
    private Long shelter_id; // 보호소 id

    // 기존 FindPhotoRequest 필드
    private String path_url; // 이미지 경로
}
