package com.ecotravel.avantura.controller;

import com.ecotravel.avantura.dto.request.*;
import com.ecotravel.avantura.dto.response.*;
import com.ecotravel.avantura.entity.Difficulty;
import com.ecotravel.avantura.service.AdventureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/adventures")
@RequiredArgsConstructor
@Tag(name = "Avanture", description = "API za upravljanje avanturama")
public class AdventureController {

    private final AdventureService adventureService;

    @PostMapping
    @Operation(summary = "Kreiranje nove avanture")
    public ResponseEntity<AdventureResponseDTO> create(@Valid @RequestBody AdventureRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adventureService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Preuzimanje avantura sa opcionalnim filtrima")
    public ResponseEntity<List<AdventureResponseDTO>> findAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(adventureService.findWithFilters(categoryId, difficulty, maxPrice));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Preuzimanje avanture po ID-u")
    public ResponseEntity<AdventureResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(adventureService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Azuriranje avanture")
    public ResponseEntity<AdventureResponseDTO> update(@PathVariable Long id,
                                                        @Valid @RequestBody AdventureRequestDTO dto) {
        return ResponseEntity.ok(adventureService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje avanture")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adventureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/time-slots")
    @Operation(summary = "Dodavanje termina avantiri")
    public ResponseEntity<TimeSlotResponseDTO> addTimeSlot(@PathVariable Long id,
                                                            @Valid @RequestBody TimeSlotRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adventureService.addTimeSlot(id, dto));
    }

    @GetMapping("/{id}/time-slots/available")
    @Operation(summary = "Preuzimanje dostupnih termina avanture")
    public ResponseEntity<List<TimeSlotResponseDTO>> getAvailableSlots(@PathVariable Long id) {
        return ResponseEntity.ok(adventureService.getAvailableTimeSlots(id));
    }

    @PostMapping("/{id}/time-slots/{slotId}/book")
    @Operation(summary = "Rezervacija mesta u terminu")
    public ResponseEntity<TimeSlotResponseDTO> bookSlot(@PathVariable Long id,
                                                         @PathVariable Long slotId) {
        return ResponseEntity.ok(adventureService.bookSlot(id, slotId));
    }

    @PostMapping("/{id}/gear")
    @Operation(summary = "Dodavanje opreme avantiri")
    public ResponseEntity<GearRequirementResponseDTO> addGear(@PathVariable Long id,
                                                               @Valid @RequestBody GearRequirementRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adventureService.addGearRequirement(id, dto));
    }

    @PostMapping("/{id}/reviews")
    @Operation(summary = "Dodavanje recenzije avanturi")
    public ResponseEntity<AdventureReviewResponseDTO> addReview(@PathVariable Long id,
                                                                  @Valid @RequestBody AdventureReviewRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adventureService.addReview(id, dto));
    }

    @GetMapping("/{id}/rating")
    @Operation(summary = "Preuzimanje prosecne ocene avanture")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        return ResponseEntity.ok(adventureService.getAverageRating(id));
    }
}
