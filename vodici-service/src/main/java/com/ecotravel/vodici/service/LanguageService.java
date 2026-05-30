package com.ecotravel.vodici.service;

import com.ecotravel.vodici.dto.request.LanguageRequestDTO;
import com.ecotravel.vodici.dto.response.LanguageResponseDTO;
import com.ecotravel.vodici.entity.Language;
import com.ecotravel.vodici.exception.BusinessException;
import com.ecotravel.vodici.exception.ResourceNotFoundException;
import com.ecotravel.vodici.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@RefreshScope
public class LanguageService {

    private final LanguageRepository languageRepository;

    @Transactional
    public LanguageResponseDTO create(LanguageRequestDTO dto) {
        if (languageRepository.existsByLanguageNameIgnoreCase(dto.getLanguageName())) {
            throw new BusinessException("Jezik '" + dto.getLanguageName() + "' vec postoji");
        }
        if (languageRepository.existsByLanguageCodeIgnoreCase(dto.getLanguageCode())) {
            throw new BusinessException("Kod jezika '" + dto.getLanguageCode() + "' vec postoji");
        }
        Language language = new Language();
        language.setLanguageName(dto.getLanguageName());
        language.setLanguageCode(dto.getLanguageCode().toUpperCase());
        return toDTO(languageRepository.save(language));
    }

    @Transactional(readOnly = true)
    public List<LanguageResponseDTO> findAll() {
        return languageRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public LanguageResponseDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    public Language getOrThrow(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jezik sa ID " + id + " nije pronadjen"));
    }

    public LanguageResponseDTO toDTO(Language language) {
        return new LanguageResponseDTO(
                language.getId(),
                language.getLanguageName(),
                language.getLanguageCode()
        );
    }
}
