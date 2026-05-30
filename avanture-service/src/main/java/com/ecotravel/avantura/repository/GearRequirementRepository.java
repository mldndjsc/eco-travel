package com.ecotravel.avantura.repository;

import com.ecotravel.avantura.entity.GearRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GearRequirementRepository extends JpaRepository<GearRequirement, Long> {
    List<GearRequirement> findByAdventureId(Long adventureId);
}
