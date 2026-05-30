package com.ecotravel.vodici.repository;

import com.ecotravel.vodici.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByGuideId(Long guideId);

    @Query("SELECT c FROM Certification c WHERE c.guide.id = :guideId AND c.expiryDate >= :today")
    List<Certification> findValidByGuideId(@Param("guideId") Long guideId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(c) FROM Certification c WHERE c.guide.id = :guideId AND c.expiryDate >= :today")
    long countValidByGuideId(@Param("guideId") Long guideId, @Param("today") LocalDate today);
}
