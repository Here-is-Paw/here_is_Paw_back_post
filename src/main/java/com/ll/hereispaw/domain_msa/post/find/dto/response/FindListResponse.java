package com.ll.hereispaw.domain_msa.post.find.dto.response;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindListResponse {
    private Long id;

    // 필수 값
    private String breed; // 견종
    private String location; // 지역
    private String etc; // 기타 특징

    private Double x;  // Point.getX() 대신 사용
    private Double y;  // Point.getY() 대신 사용

    private String pathUrl;

    public FindListResponse(Finding finding) {
        this.id = finding.getId();
        this.location = finding.getLocation();
        this.breed = finding.getBreed();
        this.etc = finding.getEtc();

        // Point 객체에서 x, y 좌표 추출
        if (finding.getGeo() != null) {
            this.x = finding.getGeo().getX();
            this.y = finding.getGeo().getY();
        }else {
            throw new CustomException(ErrorCode.GEO_INVALID_DATA);
        }

        this.pathUrl = finding.getPathUrl();
    }

}
