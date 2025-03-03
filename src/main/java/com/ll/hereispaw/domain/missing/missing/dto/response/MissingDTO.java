package com.ll.hereispaw.domain.missing.missing.dto.response;

import com.ll.hereispaw.domain.missing.missing.entity.Missing;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissingDTO {
    /**
     * 이름, 견종, 유기견 이미지, 지역, 좌표
     * 색상, 동물 등록 번호, 성별, 중성화 유무, 나이, 실종 날짜, 기타(특징), 사례금
     */
    private Long id;

    // 필수값
    private String name;
    private String breed;
    private String geo;
    private String location;

    private String color;
    private String serialNumber;
    private boolean gender;
    private boolean neutered;
    private int age;
    private Timestamp lostDate;
    private String etc;
    private int reward;
    private int state;

    private String pathUrl;
    private String nickname;

    public MissingDTO(Missing missing) {
        id = missing.getId();
        nickname = missing.getAuthor().getNickname();
        pathUrl = missing.getPathUrl();

        name = missing.getName();
        breed = missing.getBreed();
        geo = missing.getGeo();
        location = missing.getLocation();
        color = missing.getColor();
        serialNumber = missing.getSerialNumber();
        gender = missing.isGender();
        neutered = missing.isNeutered();
        age = missing.getAge();
        lostDate = missing.getLostDate();
        etc = missing.getEtc();
        reward = missing.getReward();
        state = missing.getState();
    }
}
