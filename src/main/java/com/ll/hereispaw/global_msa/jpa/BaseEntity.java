package com.ll.hereispaw.global_msa.jpa;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY) // AUTO_INCREMENT
    @Setter(AccessLevel.PROTECTED)
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    //@Setter(AccessLevel.PROTECTED)
    private LocalDateTime createdDate;

    @LastModifiedDate
    //@Setter(AccessLevel.PROTECTED)
    private LocalDateTime modifiedDate;
}