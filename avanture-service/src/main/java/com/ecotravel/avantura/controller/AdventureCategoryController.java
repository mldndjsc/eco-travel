package com.ecotravel.avantura.controller;

import com.ecotravel.avantura.dto.request.AdventureCategoryRequestDTO;
import com.ecotravel.avantura.dto.response.AdventureCategoryResponseDTO;
import com.ecotravel.avantura.service.AdventureCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Kategorije avantura", description = "API za upravljanje kategorijama avantura")
public class AdventureCategoryController {

    private final AdventureCategoryService categoryService;

    @PostMapping
    @Operation(summary = "Kreiranje nove kategorije avanture")
    public ResponseEntity<AdventureCategoryResponseDTO> create(@Valid @RequestBody AdventureCategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Preuzimanje svih kategorija")
    public ResponseEntity<List<AdventureCategoryResponseDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Preuzimanje kategorije po ID-u")
    public ResponseEntity<AdventureCategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }
}
