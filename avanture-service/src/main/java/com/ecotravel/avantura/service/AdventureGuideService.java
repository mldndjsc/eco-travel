package com.ecotravel.avantura.service;

import com.ecotravel.avantura.client.BookingClient;
import com.ecotravel.avantura.client.SmestajClient;
import com.ecotravel.avantura.client.VodiciClient;
import com.ecotravel.avantura.dto.external.*;
import com.ecotravel.avantura.dto.response.AdventureResponseDTO;
import com.ecotravel.avantura.dto.response.AdventureStatsDTO;
import com.ecotravel.avantura.dto.response.AdventureWithGuideDTO;
import com.ecotravel.avantura.entity.Adventure;
import com.ecotravel.avantura.entity.Difficulty;
import com.ecotravel.avantura.entity.TimeSlot;
import com.ecotravel.avantura.exception.BusinessException;
import com.ecotravel.avantura.exception.ResourceNotFoundException;
import com.ecotravel.avantura.pattern.*;
import com.ecotravel.avantura.repository.AdventureRepository;
import com.ecotravel.avantura.repository.TimeSlotRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdventureGuideService {

    private final VodiciClient vodiciClient;
    private final SmestajClient smestajClient;
    private final BookingClient bookingClient;
    private final AdventureRepository adventureRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AdventureService adventureService;
    private final GuideFallbackCreator guideFallbackCreator;
    private final GuideListFallbackCreator guideListFallbackCreator;
    private final CertificationListFallbackCreator certFallbackCreator;
    private final UnitListFallbackCreator unitListFallbackCreator;
    private final BookingListFallbackCreator bookingListFallbackCreator;

    @CircuitBreaker(name = "vodici-client", fallbackMethod = "getGuideByIdFallback")
    @Retry(name = "vodici-client")
    public GuideExternalDTO getGuideById(Long guideId) {
        log.info("Poziv vodici-service: getGuideById({})", guideId);
        return vodiciClient.getGuideById(guideId);
    }

    public GuideExternalDTO getGuideByIdFallback(Long guideId, Exception e) {
        if (e instanceof CallNotPermittedException) {
            log.error("[CB OTVOREN] vodici-service nedostupan za getGuideById({})", guideId);
        }
        return guideFallbackCreator.getFallbackResponse(e);
    }

    @CircuitBreaker(name = "vodici-client", fallbackMethod = "getAllGuidesFallback")
    @Retry(name = "vodici-client")
    public List<GuideExternalDTO> getAllGuides(Double minRating, Long languageId) {
        log.info("Poziv vodici-service: getAllGuides(minRating={}, languageId={})", minRating, languageId);
        return vodiciClient.getGuides(minRating, languageId);
    }

    public List<GuideExternalDTO> getAllGuidesFallback(Double minRating, Long languageId, Exception e) {
        if (e instanceof CallNotPermittedException) {
            log.error("[CB OTVOREN] vodici-service nedostupan za getAllGuides");
        }
        return guideListFallbackCreator.getFallbackResponse(e);
    }

    @CircuitBreaker(name = "vodici-client", fallbackMethod = "getCertificationsFallback")
    @Retry(name = "vodici-client")
    public List<CertificationExternalDTO> getGuideCertifications(Long guideId, boolean validOnly) {
        log.info("Poziv vodici-service: getGuideCertifications({}, validOnly={})", guideId, validOnly);
        return vodiciClient.getGuideCertifications(guideId, validOnly);
    }

    public List<CertificationExternalDTO> getCertificationsFallback(Long guideId, boolean validOnly, Exception e) {
        if (e instanceof CallNotPermittedException) {
            log.error("[CB OTVOREN] vodici-service nedostupan za getGuideCertifications({})", guideId);
        }
        return certFallbackCreator.getFallbackResponse(e);
    }

    @CircuitBreaker(name = "smestaj-client", fallbackMethod = "getNearbyUnitsFallback")
    @Retry(name = "smestaj-client")
    public List<UnitExternalDTO> getNearbyUnits(String city, Double maxPrice) {
        log.info("Poziv smestaj-service: getNearbyUnits(city={}, maxPrice={})", city, maxPrice);
        return smestajClient.getUnits(city, null, maxPrice);
    }

    public List<UnitExternalDTO> getNearbyUnitsFallback(String city, Double maxPrice, Exception e) {
        if (e instanceof CallNotPermittedException) {
            log.error("[CB OTVOREN] smestaj-service nedostupan za getNearbyUnits");
        }
        return unitListFallbackCreator.getFallbackResponse(e);
    }

    @CircuitBreaker(name = "booking-client", fallbackMethod = "getBookingsFallback")
    @Retry(name = "booking-client")
    public List<BookingExternalDTO> getBookings(String status) {
        log.info("Poziv booking-service: getBookings(status={})", status);
        return bookingClient.getBookingsByStatus(status);
    }

    public List<BookingExternalDTO> getBookingsFallback(String status, Exception e) {
        if (e instanceof CallNotPermittedException) {
            log.error("[CB OTVOREN] booking-service nedostupan za getBookings");
        }
        return bookingListFallbackCreator.getFallbackResponse(e);
    }

    @Transactional(readOnly = true)
    public AdventureWithGuideDTO getAdventureWithGuide(Long adventureId, Long guideId) {
        AdventureResponseDTO adventure = adventureService.findById(adventureId);
        GuideExternalDTO guide = getGuideById(guideId);
        List<CertificationExternalDTO> certs = getGuideCertifications(guideId, true);

        boolean eligible;
        try {
            eligible = validateGuideEligibilityForAdventure(guide,
                    adventureRepository.findById(adventureId).orElseThrow());
        } catch (BusinessException ex) {
            eligible = false;
        }

        return new AdventureWithGuideDTO(adventure, guide, certs, eligible);
    }

    public List<GuideExternalDTO> getRecommendedGuides(Long adventureId) {
        Adventure adventure = adventureRepository.findById(adventureId)
                .orElseThrow(() -> new ResourceNotFoundException("Avantura sa ID " + adventureId + " nije pronadjena"));

        List<GuideExternalDTO> allGuides = getAllGuides(null, null);
        GuideSelectionStrategy strategy = resolveStrategy(adventure.getDifficulty());
        log.info("Primenjujem strategiju: {}", strategy.getStrategyDescription());

        return strategy.selectGuides(allGuides);
    }

    private GuideSelectionStrategy resolveStrategy(Difficulty difficulty) {
        return switch (difficulty) {
            case HARD, EXTREME -> new ExpertGuideStrategy();
            default -> new StandardGuideStrategy();
        };
    }

    public boolean validateGuideEligibilityForAdventure(GuideExternalDTO guide, Adventure adventure) {
        if (guide.getId() == null) {
            throw new BusinessException("Vodic nije dostupan — vodici-service ne odgovara");
        }
        if (adventure.getDifficulty() == Difficulty.HARD || adventure.getDifficulty() == Difficulty.EXTREME) {
            if (guide.getRating() == null || guide.getRating() < 4.0) {
                throw new BusinessException(
                        "Za avanturu tezine " + adventure.getDifficulty() +
                        " vodic mora imati rejting >= 4.0. Trenutni: " + guide.getRating());
            }
            if (guide.getValidCertificationsCount() == null || guide.getValidCertificationsCount() == 0) {
                throw new BusinessException(
                        "Vodic mora imati barem jedan vazeci sertifikat za avanturu visokog nivoa tezine");
            }
        }
        return true;
    }

    public boolean validateAdventureReadiness(Long adventureId) {
        Adventure adventure = adventureRepository.findById(adventureId)
                .orElseThrow(() -> new ResourceNotFoundException("Avantura sa ID " + adventureId + " nije pronadjena"));

        if (adventure.getCategory() == null) {
            throw new BusinessException("Avantura mora imati dodeljenu kategoriju pre izvodjenja");
        }
        if (adventure.getGearRequirements().isEmpty()) {
            throw new BusinessException("Avantura mora imati definisanu listu opreme");
        }

        boolean hasFutureAvailableSlot = adventure.getTimeSlots().stream()
                .anyMatch(slot -> slot.getStartTime().isAfter(LocalDateTime.now())
                        && slot.getCurrentParticipants() < slot.getMaxParticipants());

        if (!hasFutureAvailableSlot) {
            throw new BusinessException("Avantura nema dostupnih termina u buducnosti sa slobodnim mestima");
        }
        return true;
    }

    public void validateNoTimeSlotOverlap(Long adventureId, LocalDateTime newStart, LocalDateTime newEnd) {
        List<TimeSlot> existingSlots = timeSlotRepository.findByAdventureId(adventureId);
        boolean hasOverlap = existingSlots.stream()
                .anyMatch(slot -> newStart.isBefore(slot.getEndTime())
                        && newEnd.isAfter(slot.getStartTime()));
        if (hasOverlap) {
            throw new BusinessException(
                    "Novi termin (" + newStart + " - " + newEnd + ") preklapa se sa postojecim terminom");
        }
    }

    @Transactional(readOnly = true)
    public List<AdventureStatsDTO> getAdventureStats(Long categoryId) {
        return adventureRepository.findAdventureStats(categoryId);
    }

    @Transactional(readOnly = true)
    public List<AdventureResponseDTO> getPopularAdventures(Difficulty difficulty, long minReviews, double minRating) {
        return adventureRepository.findPopularAdventures(difficulty, minReviews, minRating)
                .stream().map(adventureService::toDTO).toList();
    }
}
