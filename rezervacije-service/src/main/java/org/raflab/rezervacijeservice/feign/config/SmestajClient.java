package org.raflab.rezervacijeservice.feign.config;

import org.raflab.rezervacijeservice.feign.dto.PriceCalculationResponse;
import org.raflab.rezervacijeservice.feign.dto.UnitResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@FeignClient(
        name = "smestaj-servis",
        url = "${smestaj.service.url}",
        configuration = SmestajFeignConfig.class
)
public interface SmestajClient {

    @GetMapping("/api/smestaj/unit/{id}")
    UnitResponse getUnitById(@PathVariable Long id);

    @GetMapping("/api/smestaj/availability/check")
    Boolean isUnitAvailable(
            @RequestParam Long unitId,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate endDate
    );

    @GetMapping("/api/smestaj/price-tier/calculate")
    PriceCalculationResponse calculatePrice(
            @RequestParam Long unitId,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate date
    );
}
