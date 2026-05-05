package org.raflab.rezervacijeservice.feign.service;

import java.time.LocalDate;

public interface UnitInfoService {

    Object getUnitDetails(Long unitId);

    Object checkAvailability(Long unitId, LocalDate startDate, LocalDate endDate);

    Object calculatePrice(Long unitId, LocalDate date);
}
