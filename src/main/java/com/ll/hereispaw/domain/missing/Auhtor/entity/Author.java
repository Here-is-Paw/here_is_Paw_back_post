package com.ll.hereispaw.domain.missing.Auhtor.entity;


import com.ll.hereispaw.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "MEMBER")
//@Tag(name = "MEMBER", description = "Member")
public class Author extends BaseEntity {
//    @Column(name="nickname")
    private Long id;
    private String nickname;
}
