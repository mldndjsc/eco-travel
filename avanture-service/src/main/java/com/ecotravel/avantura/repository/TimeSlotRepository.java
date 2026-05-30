package com.ecotravel.avantura.repository;

import com.ecotravel.avantura.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByAdventureId(Long adventureId);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.adventure.id = :adventureId " +
           "AND ts.currentParticipants < ts.maxParticipants " +
           "AND ts.startTime > CURRENT_TIMESTAMP")
    List<TimeSlot> findAvailableByAdventureId(@Param("adventureId") Long adventureId);
}
