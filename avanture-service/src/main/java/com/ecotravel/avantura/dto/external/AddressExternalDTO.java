package com.ecotravel.avantura.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressExternalDTO {
    private Long id;
    private String city;
    private String zipCode;
    private String streetAddress;
}
