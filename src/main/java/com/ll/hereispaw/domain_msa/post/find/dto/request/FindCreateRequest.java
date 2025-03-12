package com.ll.hereispaw.domain_msa.post.find.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCreateRequest {
    // 필수값
    @NotBlank(message = "견종은 필수입력입니다.")
    private String breed; // 견종
    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotBlank(message = "위치는 필수입력입니다.")
    private String location;

    // 선택값
    private String name; // 이름
    private String color; // 색상
    private String serialNumber; // 등록 번호
    private Integer gender; // 성별
    private Integer neutered; // 중성화 유무
    private Integer age; // 나이
    private LocalDateTime findDate; // 발견 시간
    private String etc; // 기타 특징
    private String detailAddr; // 상세 주소

    // finding 고유 값
    private String title; // 제목
    private String situation; // 발견 상황
    private Long shelter_id; // 보호소 id

}
