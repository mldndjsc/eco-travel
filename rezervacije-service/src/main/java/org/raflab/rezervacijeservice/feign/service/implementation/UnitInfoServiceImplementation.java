package org.raflab.rezervacijeservice.feign.service.implementation;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.feign.config.SmestajClient;
import org.raflab.rezervacijeservice.feign.dto.PriceCalculationResponse;
import org.raflab.rezervacijeservice.feign.dto.UnitResponse;
import org.raflab.rezervacijeservice.feign.service.UnitInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnitInfoServiceImplementation implements UnitInfoService {

    private final SmestajClient smestajClient;

    @Override
    @CircuitBreaker(name = "smestajService", fallbackMethod = "getUnitDetailsFallback")
    @RateLimiter(name = "smestajServiceRateLimit", fallbackMethod = "getUnitDetailsRateLimitFallback")
    @Retry(name = "smestajService")
    public Object getUnitDetails(Long unitId) {
        try {
            UnitResponse unit = smestajClient.getUnitById(unitId);
            return unit;
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unit with id " + unitId + " not found in Smestaj-servis");
        } catch (Exception ex) {
            return fallbackResponse(unitId, "Unit details are temporarily unavailable.");
        }
    }

    @Override
    @CircuitBreaker(name = "smestajService", fallbackMethod = "checkAvailabilityFallback")
    @RateLimiter(name = "smestajServiceRateLimit", fallbackMethod = "checkAvailabilityRateLimitFallback")
    @Retry(name = "smestajService")
    public Object checkAvailability(Long unitId, LocalDate startDate, LocalDate endDate) {
        try {
            Boolean available = smestajClient.isUnitAvailable(unitId, startDate, endDate);
            Map<String, Object> response = new HashMap<>();
            response.put("unitId", unitId);
            response.put("available", available);
            response.put("startDate", startDate);
            response.put("endDate", endDate);
            return response;
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unit with id " + unitId + " not found in Smestaj-servis");
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("unitId", unitId);
            response.put("available", null);
            response.put("startDate", startDate);
            response.put("endDate", endDate);
            response.put("message", "Availability check is temporarily unavailable.");
            return response;
        }
    }

    @Override
    @CircuitBreaker(name = "smestajService", fallbackMethod = "calculatePriceFallback")
    @RateLimiter(name = "smestajServiceRateLimit", fallbackMethod = "calculatePriceRateLimitFallback")
    @Retry(name = "smestajService")
    public Object calculatePrice(Long unitId, LocalDate date) {
        try {
            PriceCalculationResponse response = smestajClient.calculatePrice(unitId, date);
            return response;
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unit with id " + unitId + " not found in Smestaj-servis");
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("unitId", unitId);
            response.put("date", date);
            response.put("message", "Price calculation is temporarily unavailable.");
            return response;
        }
    }

    public Object getUnitDetailsFallback(Long unitId, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unit with id " + unitId + " not found in Smestaj-servis");
        }
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }
        return fallbackResponse(unitId, "Unit details are temporarily unavailable.");
    }

    public Object checkAvailabilityFallback(Long unitId, LocalDate startDate, LocalDate endDate, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unit with id " + unitId + " not found in Smestaj-servis");
        }
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("unitId", unitId);
        response.put("available", null);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("message", "Availability check is temporarily unavailable.");
        return response;
    }

    public Object calculatePriceFallback(Long unitId, LocalDate date, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unit with id " + unitId + " not found in Smestaj-servis");
        }
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("unitId", unitId);
        response.put("date", date);
        response.put("message", "Price calculation is temporarily unavailable.");
        return response;
    }

    public Object getUnitDetailsRateLimitFallback(Long unitId, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema Smestaj-servisu. Pokusajte ponovo kasnije."
        );
    }

    public Object checkAvailabilityRateLimitFallback(Long unitId, LocalDate startDate, LocalDate endDate, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema Smestaj-servisu. Provera dostupnosti trenutno nije moguca."
        );
    }

    public Object calculatePriceRateLimitFallback(Long unitId, LocalDate date, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema Smestaj-servisu. Obracun cene trenutno nije moguc."
        );
    }

    private Map<String, Object> fallbackResponse(Long unitId, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("unitId", unitId);
        response.put("message", message);
        return response;
    }
}
