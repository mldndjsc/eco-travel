package org.raflab.rezervacijeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoicesResponseDto {

    private Long id;
    private Double subtotal;
    private Integer bookingId;
    private TaxRatesResponseDto taxRatesRequest;
    private Double total;
    private String status;

}
