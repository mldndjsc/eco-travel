package org.raflab.finansijeservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvoicesRequestDto {

    @NotNull
    private String invoiceNumber;
    @NotNull
    private Double subtotal;
    @NotNull
    private Long bookingId;
    @NotNull
    private TaxRatesRequestDto taxRatesRequest;
    private Double total;
    private String status;



}
