package com.ecotravel.avantura.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CertificationExternalDTO {
    private Long id;
    private String name;
    private String issuingBody;
    private LocalDate expiryDate;
    private Boolean isValid;
}
