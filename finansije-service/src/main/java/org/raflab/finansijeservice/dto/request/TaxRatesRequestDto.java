package org.raflab.finansijeservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaxRatesRequestDto {

    @NotEmpty(message = "Morate uneti naziv takse")
    private String taxName;
    @NotNull(message = "Morate uneti procetan takse")
    private Double percentage;
}
