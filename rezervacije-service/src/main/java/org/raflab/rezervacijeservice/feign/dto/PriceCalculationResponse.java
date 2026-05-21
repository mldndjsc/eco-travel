package org.raflab.rezervacijeservice.feign.dto;

import java.time.LocalDate;

public record PriceCalculationResponse(
        Long unitId,
        LocalDate date,
        Double basePrice,
        Double facilityCost,
        Double multiplier,
        Double finalPrice
) {}
