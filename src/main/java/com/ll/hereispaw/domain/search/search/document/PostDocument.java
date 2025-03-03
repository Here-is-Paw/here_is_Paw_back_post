package com.ll.hereispaw.domain.search.search.document;

import com.ll.hereispaw.domain.search.search.dto.PostDto;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class PostDocument{

    @NonNull
    private final String id;
    @NonNull
    private final Long post_id;
    @NonNull
    private final String breed;
    @NonNull
    private final String location;
    private final int type; // 0 = 실종 1 = 발견
//    private final LocalDateTime createDate;
    private final String name;

    public PostDocument(PostDto postDto) {
        this.id = postDto.getId();
        this.post_id = postDto.getPost_primary();
        this.name = postDto.getName();
        this.breed = postDto.getBreed();
        this.location = postDto.getLocation();
        this.type = postDto.getType();
//        this.createDate = postDto.getCreateDate();
    }
}
