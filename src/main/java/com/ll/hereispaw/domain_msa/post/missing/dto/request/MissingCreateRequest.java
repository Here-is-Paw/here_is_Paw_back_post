package com.ll.hereispaw.domain_msa.post.missing.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ll.hereispaw.global_msa.valid.FileOrPathUrlRequired;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@FileOrPathUrlRequired
public class MissingCreateRequest {
    // 필수값
    @NotBlank(message = "이름은 필수입력입니다.")
    private String name;
    @NotBlank(message = "견종은 필수입력입니다.")
    private String breed;
    @NotBlank(message = "위치는 필수입력입니다.")
    private String geo;
    @NotBlank(message = "위치는 필수입력입니다.")
    private String location;

    // 선택값
    private String color;
    private String serialNumber;
    private Integer gender;
    private Integer neutered;
    private Integer age;
//    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
//    ISO 8601 형식의 날짜 문자열을 LocalDateTime으로 자동 변환하기 위한 설정 추가
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime lostDate;
    private String etc;

    MultipartFile file;
    String pathUrl;

    // missing 고유 값
    private Integer reward;

    public boolean hasfile() {
        return file != null;
    }
}
