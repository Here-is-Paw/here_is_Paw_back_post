package com.ll.hereispaw.domain_msa.post.find.repository;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FindRepository extends JpaRepository<Finding, Long> {
    List<Finding> findByModifiedDateBefore(LocalDateTime date);

    List<Finding> findByMemberId(Long userId);

    // 전체 검색
    @Query(value = "SELECT * FROM finding f WHERE ST_DWithin(" +
            "f.geo::geography, " +
            "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, " +
            ":radius) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f.breed) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.etc) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Finding> findWithinRadiusWithKeyword(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("keyword") String keyword);

    // 품종 검색
    @Query(value = "SELECT * FROM finding f WHERE ST_DWithin(" +
            "f.geo::geography, " +
            "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, " +
            ":radius) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f.breed) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Finding> findWithinRadiusWithKeywordSearchBreed(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("keyword") String keyword);

    @Query(value = "SELECT * FROM finding f WHERE ST_DWithin(" +
            "f.geo::geography, " +
            "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, " +
            ":radius) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f.location) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Finding> findWithinRadiusWithKeywordSearchLocation(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("keyword") String keyword);

}
