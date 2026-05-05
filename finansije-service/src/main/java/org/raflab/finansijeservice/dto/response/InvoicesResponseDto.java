package org.raflab.finansijeservice.dto.response;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.finansijeservice.model.TaxRates;

@Data
public class InvoicesResponseDto {


    private Long id;
    private String invoiceNumber;
    private Double subtotal;
    private Double total_amount;
    private String status;
    private Long bookingId;
    private TaxRatesResponseDto taxRatesResponseDto;
}
