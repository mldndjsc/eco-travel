package com.ecotravel.avantura.repository;

import com.ecotravel.avantura.entity.AdventureCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdventureCategoryRepository extends JpaRepository<AdventureCategory, Long> {
    Optional<AdventureCategory> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
