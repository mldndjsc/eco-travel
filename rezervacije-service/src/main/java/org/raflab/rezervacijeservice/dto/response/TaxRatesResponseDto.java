package org.raflab.rezervacijeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxRatesResponseDto {

    private Long id;
    private String taxName;
    private Double percentage;
}
