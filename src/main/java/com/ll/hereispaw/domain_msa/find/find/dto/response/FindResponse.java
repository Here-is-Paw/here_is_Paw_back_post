package com.ll.hereispaw.domain_msa.find.find.dto.response;

import com.ll.hereispaw.domain_msa.find.find.entity.Finding;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindResponse {
    // 기존 FindRequest 필드
    private Long id;

    private String title; // 제목
    private String situation; // 발견 상황
    private String breed; // 견종
    private String location; // 지역
    private String name; // 이름
    private String color; // 색상
    private String etc; // 기타 특징

    private Double x;  // Point.getX() 대신 사용
    private Double y;  // Point.getY() 대신 사용

    private int gender; // 성별
    private int age; // 나이
    private int neutered; // 중성화 유무

    private LocalDateTime find_date; // 발견 시간

    private Long member_id; // 신고한 회원 id
    private Long shelter_id; // 보호소 id

    private String path_url;

    public FindResponse(Finding finding) {
        this.id = finding.getId();
        this.title = finding.getTitle();
        this.situation = finding.getSituation();
        this.breed = finding.getBreed();
        this.location = finding.getLocation();
        this.name = finding.getName();
        this.color = finding.getColor();
        this.etc = finding.getEtc();

        // Point 객체에서 x, y 좌표 추출
        if (finding.getGeo() != null) {
            this.x = finding.getGeo().getX();
            this.y = finding.getGeo().getY();
        }

        this.gender = finding.getGender();
        this.age = finding.getAge();
        this.neutered = finding.getNeutered();
        this.find_date = finding.getFindDate();
        this.member_id = finding.getMemberId();
        this.shelter_id = finding.getShelterId();
        this.path_url = finding.getPathUrl();
    }

}
