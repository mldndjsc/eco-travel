package com.ecotravel.vodici.repository;

import com.ecotravel.vodici.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByLanguageCodeIgnoreCase(String code);
    boolean existsByLanguageNameIgnoreCase(String name);
    boolean existsByLanguageCodeIgnoreCase(String code);
}
