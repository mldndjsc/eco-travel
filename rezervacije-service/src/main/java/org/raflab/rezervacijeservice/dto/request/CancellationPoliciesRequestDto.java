package org.raflab.rezervacijeservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancellationPoliciesRequestDto {

    @NotEmpty(message = "Morate uneti ime politike otkazivanja")
    private String policyName;
    @NotNull(message = "Morate uneti procenat iznosa koji se placa usled otkazivanja")
    @Min(value = 0, message = "Procenat mora biti bar 0")
    private int refundPercentage;
}
