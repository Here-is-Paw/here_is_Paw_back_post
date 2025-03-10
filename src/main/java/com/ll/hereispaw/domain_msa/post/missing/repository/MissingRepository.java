package com.ll.hereispaw.domain_msa.post.missing.repository;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import com.ll.hereispaw.domain_msa.post.missing.entity.Missing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissingRepository extends JpaRepository<Missing, Long> {

    List<Missing> findByMemberId(Long userId);

    // 전체 검색
    @Query(value = "SELECT * FROM missing m WHERE ST_DWithin(" +
            "m.geo::geography, " +
            "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, " +
            ":radius) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(m.breed) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.etc) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Missing> findWithinRadiusWithKeyword(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("keyword") String keyword);

    // 품종 검색
    @Query(value = "SELECT * FROM missing m WHERE ST_DWithin(" +
            "m.geo::geography, " +
            "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, " +
            ":radius) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(m.breed) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Missing> findWithinRadiusWithKeywordSearchBreed(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("keyword") String keyword);

    @Query(value = "SELECT * FROM missing m WHERE ST_DWithin(" +
            "m.geo::geography, " +
            "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, " +
            ":radius) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(m.location) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Missing> findWithinRadiusWithKeywordSearchLocation(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("keyword") String keyword);

}
