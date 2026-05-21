package org.raflab.rezervacijeservice.feign.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.feign.service.BookingFinanceService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rezervacije")
@RequiredArgsConstructor
@Tag(name = "Booking Finance", description = "Read-only invoice data pulled from Finansije-service")
@RefreshScope
public class BookingFinanceController {

    private final BookingFinanceService bookingFinanceService;

    @GetMapping("/booking/finance/{bookingId}/invoices")
    @Operation(summary = "Get invoices for a booking")
    public ResponseEntity<Object> getInvoicesForBooking(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingFinanceService.getInvoicesForBooking(bookingId));
    }
}
