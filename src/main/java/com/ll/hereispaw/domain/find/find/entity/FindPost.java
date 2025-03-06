package com.ll.hereispaw.domain.find.find.entity;

import com.ll.hereispaw.global.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;


import java.time.LocalDateTime;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "find_post")
public class FindPost extends BaseEntity {

    private String title; // 글 제목
    private String situation; // 발견 상황
    private String breed; // 견종
    private String location; // 지역
    private String name; // 이름
    private String color; // 색상
    private String etc; // 기타 특징

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geo; // 좌표인데 타입 확인 필요

    private int gender; // 성별
    private int age; // 나이
    private int state; // 상태
    private int neutered; // 중성화 유무

    private LocalDateTime find_date; // 발견 시간

    private Long member_id; // 신고한 회원 id
    private Long shelter_id; // 보호소 id

    private String path_url;

    public FindPost() {

    }
}
