package org.raflab.finansijeservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentMethodsRequestDto {

    @NotNull(message = "Unesite metod placanja (kartica, kes, kripto, itd...)")
    private String methodName;
    @NotNull(message = "Unesite da li je metod aktivan")
    private Boolean isActive;
}
