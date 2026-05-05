package org.raflab.finansijeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponseDto {

    private Long id;
    private Integer unitId;
    private Double totalPrice;
    private String status;
}
