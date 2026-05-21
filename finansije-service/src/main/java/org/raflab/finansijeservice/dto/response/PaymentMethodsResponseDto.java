package org.raflab.finansijeservice.dto.response;

import lombok.Data;

@Data
public class PaymentMethodsResponseDto {

    private Long id;
    private String methodName;
    private Boolean isActive;
}
