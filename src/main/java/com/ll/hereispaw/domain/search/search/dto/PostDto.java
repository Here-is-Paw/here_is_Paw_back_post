package com.ll.hereispaw.domain.search.search.dto;

import com.ll.hereispaw.domain.find.find.entity.FindPost;
import com.ll.hereispaw.domain.missing.missing.dto.response.MissingDTO;
import com.ll.hereispaw.global.error.ErrorCode;
import com.ll.hereispaw.global.exception.CustomException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PostDto {
    private String id;
    private Long post_id;
    private String name;
    private String breed;
    private String location;
    private int type; // 0 = 실종 1 = 발견

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createDate;

    public PostDto(Object post) {
        if (post instanceof FindPost) {
            setFind((FindPost) post);
        }else if (post instanceof MissingDTO) {
            setMissing((MissingDTO) post);
        }else {
            throw new CustomException(ErrorCode.INVALID_TYPE_VALUE);
        }
    }

    private void setFind(FindPost post) {
        log.debug("find : {}", post.getId());
        this.id = "find_" + post.getId();
        this.post_id = post.getId();
        this.name = post.getName();
        this.breed = post.getBreed();
        this.location = post.getLocation();
        this.type = 1;
//        this.createDate = LocalDateTime.now();
    }

    private void setMissing(MissingDTO post) {
        log.debug("missing : {}", post.getId());
        this.id = "missing_" + post.getId();
        this.post_id = post.getId();
        this.name = post.getName();
        this.breed = post.getBreed();
        this.location = post.getLocation();
        this.type = 0;
//        this.createDate = LocalDateTime.now();
    }
}
