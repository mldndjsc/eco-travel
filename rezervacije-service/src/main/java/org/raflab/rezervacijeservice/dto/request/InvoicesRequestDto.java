package org.raflab.rezervacijeservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raflab.rezervacijeservice.dto.response.TaxRatesResponseDto;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoicesRequestDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private Double subtotal;
    private Long bookingId;
    private TaxRatesResponseDto taxRatesRequest;
    private Double total;
    private String status;

}
