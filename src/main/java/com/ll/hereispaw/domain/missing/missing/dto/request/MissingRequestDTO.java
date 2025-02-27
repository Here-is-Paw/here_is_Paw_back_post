package com.ll.hereispaw.domain.missing.missing.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ll.hereispaw.domain.missing.Auhtor.entity.Author;
import com.ll.hereispaw.domain.missing.missing.entity.Missing;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class MissingRequestDTO {
    /**
     * 이름, 견종, 유기견 이미지, 지역, 좌표
     * 색상, 동물 등록 번호, 성별, 중성화 유무, 나이, 실종 날짜, 기타(특징), 사례금
     */
    // 필수값
    @NotBlank(message = "이름은 필수입력입니다.")
    private String name;
    @NotBlank(message = "견종은 필수입력입니다.")
    private String breed;
    @NotBlank(message = "위치는 필수입력입니다.")
    private String geo;
    @NotBlank(message = "위치는 필수입력입니다.")
    private String location;
    @NotNull(message = "사진은 필수입력입니다.")
    MultipartFile file;

    private String color;
    private String serialNumber;
    private boolean gender;
    private boolean neutered;
    private int age;
//    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    // ISO 8601 형식의 날짜 문자열을 LocalDateTime으로 자동 변환하기 위한 설정 추가
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime lostDate;
    private String etc;
    private int reward;
    private int missingState;
}
