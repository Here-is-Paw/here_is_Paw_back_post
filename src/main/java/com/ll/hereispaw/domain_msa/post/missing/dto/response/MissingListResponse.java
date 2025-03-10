package com.ll.hereispaw.domain_msa.post.missing.dto.response;

import com.ll.hereispaw.domain_msa.post.missing.entity.Missing;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissingListResponse {
    private Long id;

    // 필수값
    private String breed;
    private String location;
    private String etc;

    private Double x;  // Point.getX() 대신 사용
    private Double y;  // Point.getY() 대신 사용

    private String pathUrl;

    public MissingListResponse(Missing missing) {
        id = missing.getId();
        location = missing.getLocation();
        breed = missing.getBreed();
        etc = missing.getEtc();

        if (missing.getGeo() != null) {
            this.x = missing.getGeo().getX();
            this.y = missing.getGeo().getY();
        }else {
            throw new CustomException(ErrorCode.GEO_INVALID_DATA);
        }

        pathUrl = missing.getPathUrl();
    }
}
