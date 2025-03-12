package com.ll.hereispaw.domain_msa.post.find.dto.response;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import java.time.LocalDateTime;

import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindResponse {
    private Long id;

    // 유저 정보
    private Long memberId;
    private String nickname;

    // 필수 값
    private String breed; // 견종
    private String location; // 지역
    private Double x;  // Point.getX() 대신 사용
    private Double y;  // Point.getY() 대신 사용
    private String pathUrl;

    // 선택 값
    private String name; // 이름
    private String serialNumber;
    private String color;
    private Integer gender; // 성별
    private Integer age; // 나이
    private Integer neutered; // 중성화 유무
    private String etc; // 기타 특징
    private LocalDateTime findDate; // 발견 시간
    private String detailAddr; // 상세 주소

    // 고유 finding 값
    private String title; // 제목
    private String situation; // 발견 상황
    private Long shelterId; // 보호소 id


    public FindResponse(Finding finding) {
        this.id = finding.getId();

        this.memberId = finding.getMemberId();
        this.nickname = finding.getNickname();

        this.breed = finding.getBreed();
        this.location = finding.getLocation();
        // Point 객체에서 x, y 좌표 추출
        if (finding.getGeo() != null) {
            this.x = finding.getGeo().getX();
            this.y = finding.getGeo().getY();
        }else {
            throw new CustomException(ErrorCode.GEO_INVALID_DATA);
        }
        this.pathUrl = finding.getPathUrl();

        this.name = finding.getName();
        this.serialNumber = finding.getSerialNumber();
        this.color = finding.getColor();
        this.gender = finding.getGender();
        this.age = finding.getAge();
        this.neutered = finding.getNeutered();
        this.etc = finding.getEtc();
        this.findDate = finding.getFindDate();
        this.detailAddr = finding.getDetailAddr(); // 상세 주소
        this.title = finding.getTitle();
        this.situation = finding.getSituation();
        this.shelterId = finding.getShelterId();
    }

}
