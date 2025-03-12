package com.ll.hereispaw.domain_msa.post.find.entity;


import com.ll.hereispaw.global_msa.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Finding extends BaseEntity {

    // 필수값
    @NotNull
    @Column(length = 50)
    private String breed; // 견종
    @NotNull
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point geo;
    @NotNull
    private String location;
    @NotNull
    private String pathUrl;

    // 선택 값
    @Column(length = 50)
    private String name; // 이름
    @Column(length = 50)
    private String color; // 색상
    @Column(length = 15)
    private String serialNumber;
    private String detailAddr; // 상세주소

    private Integer gender; // 성별
    private Integer neutered; // 중성화 유무
    private Integer age; // 나이

    private Long memberId; // 신고한 회원 id

    @Column(length = 15)
    private String nickname;
    @Column(columnDefinition = "TEXT")
    private String etc; // 기타 특징

    private LocalDateTime findDate; // 발견 시간

    // fing 개별
    private String title; // 글 제목
    private String situation; // 발견 상황

    private Integer state; // 상태
    private Long shelterId; // 보호소 id
}
