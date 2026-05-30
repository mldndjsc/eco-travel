package com.ecotravel.avantura.service;

import com.ecotravel.avantura.dto.request.AdventureCategoryRequestDTO;
import com.ecotravel.avantura.dto.response.AdventureCategoryResponseDTO;
import com.ecotravel.avantura.entity.AdventureCategory;
import com.ecotravel.avantura.exception.BusinessException;
import com.ecotravel.avantura.exception.ResourceNotFoundException;
import com.ecotravel.avantura.repository.AdventureCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdventureCategoryService {

    private final AdventureCategoryRepository categoryRepository;

    @Transactional
    public AdventureCategoryResponseDTO create(AdventureCategoryRequestDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BusinessException("Kategorija sa nazivom '" + dto.getName() + "' vec postoji");
        }
        AdventureCategory category = new AdventureCategory();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return toDTO(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<AdventureCategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public AdventureCategoryResponseDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    public AdventureCategory getOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija sa ID " + id + " nije pronadjena"));
    }

    AdventureCategoryResponseDTO toDTO(AdventureCategory category) {
        return new AdventureCategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
