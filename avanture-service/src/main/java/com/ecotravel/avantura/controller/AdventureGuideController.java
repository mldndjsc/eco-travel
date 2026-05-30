package com.ecotravel.avantura.controller;

import com.ecotravel.avantura.dto.external.BookingExternalDTO;
import com.ecotravel.avantura.dto.external.GuideExternalDTO;
import com.ecotravel.avantura.dto.external.UnitExternalDTO;
import com.ecotravel.avantura.dto.response.AdventureResponseDTO;
import com.ecotravel.avantura.dto.response.AdventureStatsDTO;
import com.ecotravel.avantura.dto.response.AdventureWithGuideDTO;
import com.ecotravel.avantura.entity.Difficulty;
import com.ecotravel.avantura.service.AdventureGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adventures")
@RequiredArgsConstructor
@Tag(name = "Avanture i Vodici", description = "API za integraciju avantura i vodica")
public class AdventureGuideController {

    private final AdventureGuideService adventureGuideService;

    @GetMapping("/{adventureId}/guide/{guideId}")
    @Operation(summary = "Preuzimanje avanture sa podacima vodica")
    public ResponseEntity<AdventureWithGuideDTO> getAdventureWithGuide(
            @PathVariable Long adventureId,
            @PathVariable Long guideId) {
        return ResponseEntity.ok(adventureGuideService.getAdventureWithGuide(adventureId, guideId));
    }

    @GetMapping("/{adventureId}/recommended-guides")
    @Operation(summary = "Preuzimanje preporucenih vodica za avanturu")
    public ResponseEntity<List<GuideExternalDTO>> getRecommendedGuides(@PathVariable Long adventureId) {
        return ResponseEntity.ok(adventureGuideService.getRecommendedGuides(adventureId));
    }

    @GetMapping("/stats")
    @Operation(summary = "Statistika avantura sa prosecnim ocenama")
    public ResponseEntity<List<AdventureStatsDTO>> getStats(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(adventureGuideService.getAdventureStats(categoryId));
    }

    @GetMapping("/popular")
    @Operation(summary = "Popularne avanture na osnovu broja i ocene recenzija")
    public ResponseEntity<List<AdventureResponseDTO>> getPopular(
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(defaultValue = "1") long minReviews,
            @RequestParam(defaultValue = "4.0") double minRating) {
        return ResponseEntity.ok(adventureGuideService.getPopularAdventures(difficulty, minReviews, minRating));
    }

    @GetMapping("/{adventureId}/readiness")
    @Operation(summary = "Provera da li je avantura spremna za izvodjenje")
    public ResponseEntity<Boolean> checkReadiness(@PathVariable Long adventureId) {
        return ResponseEntity.ok(adventureGuideService.validateAdventureReadiness(adventureId));
    }

    @GetMapping("/nearby-units")
    @Operation(summary = "Preuzimanje smestajnih jedinica u blizini avanture")
    public ResponseEntity<List<UnitExternalDTO>> getNearbyUnits(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(adventureGuideService.getNearbyUnits(city, maxPrice));
    }

    @GetMapping("/bookings")
    @Operation(summary = "Preuzimanje rezervacija iz booking servisa")
    public ResponseEntity<List<BookingExternalDTO>> getBookings(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(adventureGuideService.getBookings(status));
    }
}
