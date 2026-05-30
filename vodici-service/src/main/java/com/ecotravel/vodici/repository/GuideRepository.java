package com.ecotravel.vodici.repository;

import com.ecotravel.vodici.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    @Query("SELECT DISTINCT g FROM Guide g LEFT JOIN g.guideLanguages gl LEFT JOIN gl.language l " +
           "WHERE (:minRating IS NULL OR g.rating >= :minRating) " +
           "AND (:languageId IS NULL OR l.id = :languageId)")
    List<Guide> findWithFilters(
            @Param("minRating") Double minRating,
            @Param("languageId") Long languageId
    );
}
