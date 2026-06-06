package org.raflab.finansijeservice.client;

import org.raflab.finansijeservice.dto.response.BookingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "rezervacije-service",
        configuration = BookingFeignConfig.class
)
public interface BookingClient {

    @GetMapping("/api/rezervacije/bookings/{id}")
    BookingResponseDto findBookingById(@PathVariable Long id);

    @PutMapping("/api/rezervacije/bookings/{id}/status")
    void updateBookingStatus(@PathVariable Long id, @RequestParam String status);

    @PutMapping("/api/rezervacije/bookings/{id}/cancel-unpaid")
    void cancelBookingDueToNonPayment(@PathVariable Long id);
}
