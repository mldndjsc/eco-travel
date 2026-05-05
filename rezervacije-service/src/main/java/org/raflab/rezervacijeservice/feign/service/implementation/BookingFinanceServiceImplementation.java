package org.raflab.rezervacijeservice.feign.service.implementation;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.feign.config.FinansijeClient;
import org.raflab.rezervacijeservice.feign.dto.InvoiceResponse;
import org.raflab.rezervacijeservice.feign.service.BookingFinanceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingFinanceServiceImplementation implements BookingFinanceService {

    private final FinansijeClient finansijeClient;

    @Override
    @CircuitBreaker(name = "finansijeService", fallbackMethod = "getInvoicesForBookingFallback")
    @RateLimiter(name = "finansijeServiceRateLimit", fallbackMethod = "getInvoicesForBookingRateLimitFallback")
    @Retry(name = "finansijeService")
    public Object getInvoicesForBooking(Long bookingId) {
        List<InvoiceResponse> all = finansijeClient.getAllInvoices();
        List<InvoiceResponse> filtered = all.stream()
                .filter(i -> i.bookingId() != null && i.bookingId().equals(bookingId))
                .toList();
        return filtered;
    }

    public Object getInvoicesForBookingFallback(Long bookingId, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Invoices for booking " + bookingId + " not found in Finansije-service");
        }
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", bookingId);
        response.put("message", "Invoices are temporarily unavailable.");
        response.put("invoices", List.of());
        return response;
    }

    public Object getInvoicesForBookingRateLimitFallback(Long bookingId, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema Finansije-servisu. Pokusajte ponovo kasnije."
        );
    }
}
