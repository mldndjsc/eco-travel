package com.ecotravel.avantura.service;

import com.ecotravel.avantura.dto.request.AdventureRequestDTO;
import com.ecotravel.avantura.dto.request.AdventureReviewRequestDTO;
import com.ecotravel.avantura.dto.request.GearRequirementRequestDTO;
import com.ecotravel.avantura.dto.request.TimeSlotRequestDTO;
import com.ecotravel.avantura.dto.response.*;
import com.ecotravel.avantura.entity.*;
import com.ecotravel.avantura.exception.BusinessException;
import com.ecotravel.avantura.exception.ResourceNotFoundException;
import com.ecotravel.avantura.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@RefreshScope
public class AdventureService {

    private final AdventureRepository adventureRepository;
    private final AdventureCategoryService categoryService;
    private final TimeSlotRepository timeSlotRepository;
    private final GearRequirementRepository gearRequirementRepository;
    private final AdventureReviewRepository reviewRepository;

    @Transactional
    public AdventureResponseDTO create(AdventureRequestDTO dto) {
        Adventure adventure = new Adventure();
        adventure.setTitle(dto.getTitle());
        adventure.setDescription(dto.getDescription());
        adventure.setDifficulty(dto.getDifficulty());
        adventure.setBasePrice(dto.getBasePrice());
        if (dto.getCategoryId() != null) {
            adventure.setCategory(categoryService.getOrThrow(dto.getCategoryId()));
        }
        return toDTO(adventureRepository.save(adventure));
    }

    @Transactional(readOnly = true)
    public List<AdventureResponseDTO> findWithFilters(Long categoryId, Difficulty difficulty, BigDecimal maxPrice) {
        return adventureRepository.findWithFilters(categoryId, difficulty, maxPrice)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public AdventureResponseDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    @Transactional
    public AdventureResponseDTO update(Long id, AdventureRequestDTO dto) {
        Adventure adventure = getOrThrow(id);
        adventure.setTitle(dto.getTitle());
        adventure.setDescription(dto.getDescription());
        adventure.setDifficulty(dto.getDifficulty());
        adventure.setBasePrice(dto.getBasePrice());
        if (dto.getCategoryId() != null) {
            adventure.setCategory(categoryService.getOrThrow(dto.getCategoryId()));
        }
        return toDTO(adventureRepository.save(adventure));
    }

    @Transactional
    public void delete(Long id) {
        if (!adventureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avantura sa ID " + id + " nije pronadjena");
        }
        adventureRepository.deleteById(id);
    }

    @Transactional
    public TimeSlotResponseDTO addTimeSlot(Long adventureId, TimeSlotRequestDTO dto) {
        Adventure adventure = getOrThrow(adventureId);
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BusinessException("Vreme pocetka mora biti pre vremena zavrsetka");
        }
        TimeSlot slot = new TimeSlot();
        slot.setAdventure(adventure);
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setMaxParticipants(dto.getMaxParticipants());
        slot.setCurrentParticipants(0);
        return toSlotDTO(timeSlotRepository.save(slot));
    }

    @Transactional(readOnly = true)
    public List<TimeSlotResponseDTO> getAvailableTimeSlots(Long adventureId) {
        getOrThrow(adventureId);
        return timeSlotRepository.findAvailableByAdventureId(adventureId)
                .stream().map(this::toSlotDTO).toList();
    }

    @Transactional
    public TimeSlotResponseDTO bookSlot(Long adventureId, Long slotId) {
        getOrThrow(adventureId);
        TimeSlot slot = timeSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Termin sa ID " + slotId + " nije pronadjen"));
        if (slot.getCurrentParticipants() >= slot.getMaxParticipants()) {
            throw new BusinessException("Termin je popunjen");
        }
        slot.setCurrentParticipants(slot.getCurrentParticipants() + 1);
        return toSlotDTO(timeSlotRepository.save(slot));
    }

    @Transactional
    public GearRequirementResponseDTO addGearRequirement(Long adventureId, GearRequirementRequestDTO dto) {
        Adventure adventure = getOrThrow(adventureId);
        GearRequirement gear = new GearRequirement();
        gear.setAdventure(adventure);
        gear.setName(dto.getName());
        gear.setDescription(dto.getDescription());
        gear.setIsMandatory(dto.getIsMandatory() != null ? dto.getIsMandatory() : true);
        return toGearDTO(gearRequirementRepository.save(gear));
    }

    @Transactional
    public AdventureReviewResponseDTO addReview(Long adventureId, AdventureReviewRequestDTO dto) {
        Adventure adventure = getOrThrow(adventureId);
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new BusinessException("Ocena mora biti izmedju 1 i 5");
        }
        AdventureReview review = new AdventureReview();
        review.setAdventure(adventure);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewDate(LocalDate.now());
        review.setReviewerName(dto.getReviewerName());
        return toReviewDTO(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public Double getAverageRating(Long adventureId) {
        getOrThrow(adventureId);
        return reviewRepository.calculateAverageRating(adventureId).orElse(0.0);
    }

    public Adventure getOrThrow(Long id) {
        return adventureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avantura sa ID " + id + " nije pronadjena"));
    }

    AdventureResponseDTO toDTO(Adventure adventure) {
        Double avgRating = reviewRepository.calculateAverageRating(adventure.getId()).orElse(0.0);
        long reviewCount = adventure.getReviews().size();

        return new AdventureResponseDTO(
                adventure.getId(),
                adventure.getTitle(),
                adventure.getDescription(),
                adventure.getDifficulty(),
                adventure.getBasePrice(),
                adventure.getCategory() != null ? categoryService.toDTO(adventure.getCategory()) : null,
                adventure.getGearRequirements().stream().map(this::toGearDTO).toList(),
                avgRating,
                reviewCount
        );
    }

    private TimeSlotResponseDTO toSlotDTO(TimeSlot slot) {
        return new TimeSlotResponseDTO(
                slot.getId(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getMaxParticipants(),
                slot.getCurrentParticipants(),
                slot.getMaxParticipants() - slot.getCurrentParticipants()
        );
    }

    private GearRequirementResponseDTO toGearDTO(GearRequirement gear) {
        return new GearRequirementResponseDTO(
                gear.getId(),
                gear.getName(),
                gear.getDescription(),
                gear.getIsMandatory()
        );
    }

    private AdventureReviewResponseDTO toReviewDTO(AdventureReview review) {
        return new AdventureReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate(),
                review.getReviewerName()
        );
    }
}
