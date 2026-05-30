package com.ecotravel.avantura.client;

import com.ecotravel.avantura.config.FeignConfig;
import com.ecotravel.avantura.dto.external.BookingExternalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "booking-client", url = "${booking-service.url}", configuration = FeignConfig.class)
public interface BookingClient {

    @GetMapping("/api/bookings")
    List<BookingExternalDTO> getAllBookings();

    @GetMapping("/api/bookings/filter")
    List<BookingExternalDTO> getBookingsByStatus(
            @RequestParam(value = "status", required = false) String status
    );
}
