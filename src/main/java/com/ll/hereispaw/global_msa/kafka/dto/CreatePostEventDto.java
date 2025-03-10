package com.ll.hereispaw.global_msa.kafka.dto;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import com.ll.hereispaw.domain_msa.post.missing.entity.Missing;

import com.ll.hereispaw.global_msa.enums.PostType;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CreatePostEventDto {
    @NonNull
    private String id;

    @NonNull
    private Long post_id;

    @NonNull
    private String imageUrl;

    @NonNull
    private String breed;

    @NonNull
    private String location;

    @NonNull
    private Double x;

    @NonNull
    private Double y;

    private int type; // 0 = 실종 1 = 발견

    private String name;

    private String etc;

    private int state;

    public CreatePostEventDto(Object post, int state) {
        if (post instanceof Finding) {
            setFind((Finding) post, state);
        }else if (post instanceof Missing) {
            setMissing((Missing) post, state);
        }else {
            throw new CustomException(ErrorCode.INVALID_TYPE_VALUE);
        }
    }

    private void setFind(Finding post, int state) {
        log.debug("find : {}", post.getId());
        this.id = "find_" + post.getId();
        this.post_id = post.getId();
        this.imageUrl = post.getPathUrl();
        this.name = post.getName();
        this.breed = post.getBreed();
        this.x = post.getGeo().getX();
        this.y = post.getGeo().getY();
        this.location = post.getLocation();
        this.type = PostType.FIND.getCode();
        this.etc = post.getEtc();
        this.state = state;
//        this.createDate = LocalDateTime.now();
    }

    private void setMissing(Missing post, int state) {
        log.debug("missing : {}", post.getId());
        this.id = "missing_" + post.getId();
        this.post_id = post.getId();
        this.imageUrl = post.getPathUrl();
        this.name = post.getName();
        this.breed = post.getBreed();
        this.x = post.getGeo().getX();
        this.y = post.getGeo().getY();
        this.location = post.getLocation();
        this.type = PostType.MISSING.getCode();
        this.etc = post.getEtc();
        this.state = state;
//        this.createDate = LocalDateTime.now();
    }
}
