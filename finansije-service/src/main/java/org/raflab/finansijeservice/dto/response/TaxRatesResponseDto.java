package org.raflab.finansijeservice.dto.response;

import lombok.Data;

@Data
public class TaxRatesResponseDto {

    private Long id;
    private String taxName;
    private Double percentage;


}
