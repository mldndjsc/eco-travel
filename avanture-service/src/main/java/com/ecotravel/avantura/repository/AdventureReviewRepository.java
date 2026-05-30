package com.ecotravel.avantura.repository;

import com.ecotravel.avantura.entity.AdventureReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdventureReviewRepository extends JpaRepository<AdventureReview, Long> {

    List<AdventureReview> findByAdventureId(Long adventureId);

    @Query("SELECT AVG(r.rating) FROM AdventureReview r WHERE r.adventure.id = :adventureId")
    Optional<Double> calculateAverageRating(@Param("adventureId") Long adventureId);
}
