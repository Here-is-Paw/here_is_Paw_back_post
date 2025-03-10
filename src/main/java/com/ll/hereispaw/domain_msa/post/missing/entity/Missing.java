package com.ll.hereispaw.domain_msa.post.missing.entity;

import com.ll.hereispaw.global_msa.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Missing extends BaseEntity {

    // 필수값
    @NotNull
    @Column(length = 50)
    private String name;
    @NotNull
    @Column(length = 50)
    private String breed;
    @NotNull
    private Point geo;
    @NotNull
    private String location;
    @NotNull
    private String pathUrl;

    // 선택 값
    @Column(length = 50)
    private String color;
    @Column(length = 15)
    private String serialNumber;

    private Integer gender;
    private Integer neutered;
    private Integer age;

    private Long memberId;

    @Column(length = 15)
    private String nickname;
    @Column(columnDefinition = "TEXT")
    private String etc;

    private LocalDateTime lostDate;

    // missing 개별
    private Integer state; //상태
    private Integer reward;
}
