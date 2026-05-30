package com.ecotravel.avantura.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BookingExternalDTO {
    private Integer id;
    private Integer unitId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private String status;
}
