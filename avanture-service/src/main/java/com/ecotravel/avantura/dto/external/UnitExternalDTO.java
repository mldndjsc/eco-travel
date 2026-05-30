package com.ecotravel.avantura.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnitExternalDTO {
    private Long id;
    private String name;
    private String unitType;
    private Double basePricePerNight;
    private AddressExternalDTO address;
}
