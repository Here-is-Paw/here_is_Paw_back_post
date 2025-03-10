package com.ll.hereispaw.domain_msa.post.missing.dto.response;

import com.ll.hereispaw.domain_msa.post.missing.entity.Missing;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class MissingResponse {
    private Long id;

    // 유저 정보
    private Long memberId;
    private String nickname;

    // 필수값
    private String name;
    private String breed;
    private String location;
    private Double x;  // Point.getX() 대신 사용
    private Double y;  // Point.getY() 대신 사용
    private String pathUrl;

    // 선택 값
    private String serialNumber;
    private String color;
    private Integer gender;
    private Integer age;
    private Integer neutered;
    private String etc;
    private LocalDateTime lostDate;

    // 고유 missing 값
    private Integer reward;


    public MissingResponse(Missing missing) {
        id = missing.getId();

        memberId = missing.getMemberId();
        nickname = missing.getNickname();

        name = missing.getName();
        breed = missing.getBreed();
        location = missing.getLocation();
        if (missing.getGeo() != null) {
            this.x = missing.getGeo().getX();
            this.y = missing.getGeo().getY();
        }else {
            throw new CustomException(ErrorCode.GEO_INVALID_DATA);
        }
        pathUrl = missing.getPathUrl();

        serialNumber = missing.getSerialNumber();
        color = missing.getColor();
        gender = missing.getGender();
        age = missing.getAge();
        neutered = missing.getNeutered();
        etc = missing.getEtc();
        lostDate = missing.getLostDate();

        reward = missing.getReward();
    }
}
