package com.ecotravel.vodici.controller;

import com.ecotravel.vodici.dto.request.LanguageRequestDTO;
import com.ecotravel.vodici.dto.response.LanguageResponseDTO;
import com.ecotravel.vodici.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
@Tag(name = "Jezici", description = "API za upravljanje jezicima vodica")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    @Operation(summary = "Kreiranje novog jezika")
    public ResponseEntity<LanguageResponseDTO> create(@Valid @RequestBody LanguageRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Preuzimanje svih jezika")
    public ResponseEntity<List<LanguageResponseDTO>> findAll() {
        return ResponseEntity.ok(languageService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Preuzimanje jezika po ID-u")
    public ResponseEntity<LanguageResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.findById(id));
    }
}
