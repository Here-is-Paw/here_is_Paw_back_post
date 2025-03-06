package com.ll.hereispaw.domain_msa.find.find.entity;


import com.ll.hereispaw.global_msa.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Table(name = "find_post")
public class Finding extends BaseEntity {

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
    private int serialNumber; // 등록 번호
    private int age; // 나이
    private int state; // 상태
    private int neutered; // 중성화 유무

    private LocalDateTime findDate; // 발견 시간
    private Long memberId; // 신고한 회원 id
    private Long shelterId; // 보호소 id

    private String pathUrl;

    public Finding() {

    }
}
