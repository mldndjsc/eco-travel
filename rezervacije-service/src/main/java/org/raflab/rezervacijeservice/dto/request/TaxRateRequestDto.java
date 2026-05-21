package org.raflab.rezervacijeservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxRateRequestDto {

    private String taxName;
    private Double percentage;
}
