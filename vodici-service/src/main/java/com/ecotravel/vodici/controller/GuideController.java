package com.ecotravel.vodici.controller;

import com.ecotravel.vodici.dto.request.CertificationRequestDTO;
import com.ecotravel.vodici.dto.request.GuideRequestDTO;
import com.ecotravel.vodici.dto.response.CertificationResponseDTO;
import com.ecotravel.vodici.dto.response.GuideResponseDTO;
import com.ecotravel.vodici.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
@Tag(name = "Vodici", description = "API za upravljanje vodicima")
public class GuideController {

    private final GuideService guideService;

    @PostMapping
    @Operation(summary = "Kreiranje novog vodica")
    public ResponseEntity<GuideResponseDTO> create(@Valid @RequestBody GuideRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guideService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Preuzimanje svih vodica sa opcionalnim filtrima")
    public ResponseEntity<List<GuideResponseDTO>> findAll(
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Long languageId) {
        return ResponseEntity.ok(guideService.findAll(minRating, languageId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Preuzimanje vodica po ID-u")
    public ResponseEntity<GuideResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Azuriranje vodica")
    public ResponseEntity<GuideResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody GuideRequestDTO dto) {
        return ResponseEntity.ok(guideService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje vodica")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guideService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{guideId}/languages/{languageId}")
    @Operation(summary = "Dodela jezika vodicu")
    public ResponseEntity<GuideResponseDTO> assignLanguage(@PathVariable Long guideId,
                                                            @PathVariable Long languageId) {
        return ResponseEntity.ok(guideService.assignLanguage(guideId, languageId));
    }

    @DeleteMapping("/{guideId}/languages/{languageId}")
    @Operation(summary = "Uklanjanje jezika od vodica")
    public ResponseEntity<GuideResponseDTO> removeLanguage(@PathVariable Long guideId,
                                                            @PathVariable Long languageId) {
        return ResponseEntity.ok(guideService.removeLanguage(guideId, languageId));
    }

    @PostMapping("/{guideId}/certifications")
    @Operation(summary = "Dodavanje sertifikata vodicu")
    public ResponseEntity<CertificationResponseDTO> addCertification(
            @PathVariable Long guideId,
            @Valid @RequestBody CertificationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guideService.addCertification(guideId, dto));
    }

    @GetMapping("/{guideId}/certifications")
    @Operation(summary = "Preuzimanje sertifikata vodica")
    public ResponseEntity<List<CertificationResponseDTO>> getCertifications(
            @PathVariable Long guideId,
            @RequestParam(defaultValue = "false") boolean validOnly) {
        return ResponseEntity.ok(guideService.getCertifications(guideId, validOnly));
    }
}
