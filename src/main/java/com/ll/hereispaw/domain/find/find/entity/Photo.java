package com.ll.hereispaw.domain.find.find.entity;

import com.ll.hereispaw.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Photo extends BaseEntity {

    private String path_url; // 이미지 경로

    private Long postId; // 게시글 id
    private Long member_id; // 작성자 id

    public Photo() {

    }
}
