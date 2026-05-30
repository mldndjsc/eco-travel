package com.ecotravel.avantura.repository;

import com.ecotravel.avantura.dto.response.AdventureStatsDTO;
import com.ecotravel.avantura.entity.Adventure;
import com.ecotravel.avantura.entity.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    List<Adventure> findByCategoryId(Long categoryId);

    List<Adventure> findByDifficulty(Difficulty difficulty);

    List<Adventure> findByBasePriceLessThanEqual(BigDecimal maxPrice);

    @Query("SELECT a FROM Adventure a WHERE " +
           "(:categoryId IS NULL OR a.category.id = :categoryId) AND " +
           "(:difficulty IS NULL OR a.difficulty = :difficulty) AND " +
           "(:maxPrice IS NULL OR a.basePrice <= :maxPrice)")
    List<Adventure> findWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("difficulty") Difficulty difficulty,
            @Param("maxPrice") BigDecimal maxPrice
    );

    @Query("SELECT new com.ecotravel.avantura.dto.response.AdventureStatsDTO(" +
           "a.id, a.title, a.difficulty, a.basePrice, " +
           "COALESCE(AVG(r.rating), 0.0), COUNT(r.id)) " +
           "FROM Adventure a LEFT JOIN a.reviews r " +
           "WHERE (:categoryId IS NULL OR a.category.id = :categoryId) " +
           "GROUP BY a.id, a.title, a.difficulty, a.basePrice " +
           "ORDER BY AVG(r.rating) DESC")
    List<AdventureStatsDTO> findAdventureStats(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Adventure a JOIN a.reviews r " +
           "WHERE (:difficulty IS NULL OR a.difficulty = :difficulty) " +
           "GROUP BY a " +
           "HAVING COUNT(r) >= :minReviews AND AVG(r.rating) >= :minRating " +
           "ORDER BY AVG(r.rating) DESC")
    List<Adventure> findPopularAdventures(
            @Param("difficulty") Difficulty difficulty,
            @Param("minReviews") long minReviews,
            @Param("minRating") double minRating
    );
}
