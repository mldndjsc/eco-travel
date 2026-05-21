package org.raflab.rezervacijeservice.feign.dto;

import java.util.List;

public record UnitResponse(
        Long id,
        String name,
        String unitType,
        Double basePricePerNight,
        String city,
        List<String> facilities
) {}
