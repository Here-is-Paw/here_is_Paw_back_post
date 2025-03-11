package com.ll.hereispaw.domain_msa.post.userPost.dto.response;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import com.ll.hereispaw.domain_msa.post.missing.entity.Missing;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PostListDto {
    @NonNull
    private Long id;

    // 필수 값
    @NonNull
    private String breed; // 견종
    @NonNull
    private String location; // 지역

    @NonNull
    private Double x;  // Point.getX() 대신 사용
    @NonNull
    private Double y;  // Point.getY() 대신 사용

    private String etc; // 기타 특징

    @NonNull
    private String pathUrl;

    public PostListDto(Object post) {
        if (post instanceof Finding) {
            setFinding((Finding) post);
        }else if (post instanceof Missing) {
            setMissing((Missing) post);
        }else {
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    private void setFinding(Finding post) {
        this.id = post.getId();
        this.breed = post.getBreed();
        this.location = post.getLocation();
        this.x = post.getGeo().getX();
        this.y = post.getGeo().getY();
        this.etc = post.getEtc();
        this.pathUrl = post.getPathUrl();
    }

    private void setMissing(Missing post) {
        this.id = post.getId();
        this.breed = post.getBreed();
        this.location = post.getLocation();
        this.x = post.getGeo().getX();
        this.y = post.getGeo().getY();
        this.etc = post.getEtc();
        this.pathUrl = post.getPathUrl();
    }
}
