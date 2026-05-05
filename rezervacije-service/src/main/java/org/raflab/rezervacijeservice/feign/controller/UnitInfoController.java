package org.raflab.rezervacijeservice.feign.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.feign.service.UnitInfoService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rezervacije")
@RequiredArgsConstructor
@Tag(name = "Booking Unit Info", description = "Read-only unit data pulled from Smestaj-servis")
@RefreshScope
public class UnitInfoController {

    private final UnitInfoService unitInfoService;

    @GetMapping("/bookings/unit/{unitId}/details")
    @Operation(summary = "Get unit details for a booking unitId")
    public ResponseEntity<Object> getUnitDetails(
            @Parameter(description = "Unit ID") @PathVariable Long unitId) {
        return ResponseEntity.ok(unitInfoService.getUnitDetails(unitId));
    }

    @GetMapping("/bookings/unit/{unitId}/availability")
    @Operation(summary = "Check if a unit is available for a date range")
    public ResponseEntity<Object> checkAvailability(
            @Parameter(description = "Unit ID") @PathVariable Long unitId,
            @Parameter(description = "Start date yyyy-MM-dd") @RequestParam LocalDate startDate,
            @Parameter(description = "End date yyyy-MM-dd") @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(unitInfoService.checkAvailability(unitId, startDate, endDate));
    }

    @GetMapping("/bookings/unit/{unitId}/price")
    @Operation(summary = "Calculate unit price for a specific date")
    public ResponseEntity<Object> calculatePrice(
            @Parameter(description = "Unit ID") @PathVariable Long unitId,
            @Parameter(description = "Date yyyy-MM-dd") @RequestParam LocalDate date) {
        return ResponseEntity.ok(unitInfoService.calculatePrice(unitId, date));
    }
}
