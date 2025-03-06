package com.ll.hereispaw.domain.find.find.repository;

import com.ll.hereispaw.domain.find.find.entity.FindPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FindRepository extends JpaRepository<FindPost, Long> {
    List<FindPost> findByModifiedAtBefore(LocalDateTime date);
//    @Query(value = "SELECT f FROM FindPost f " +
//            "WHERE ST_DWithin(" +
//            "    ST_Point(CAST(SPLIT_PART(f.geo, ',', 1) AS DOUBLE), " +
//            "            CAST(SPLIT_PART(f.geo, ',', 2) AS DOUBLE)), " +
//            "    ST_Point(:longitude, :latitude), " +
//            "    0.005" + // 약 500m (대략적인 값, 정확한 계산 필요)
//            ")")
//    List<FindPost> findNearbyFindPosts(
//            @Param("longitude") Double longitude,
//            @Param("latitude") Double latitude
//    );
}
