package org.raflab.finansijeservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequestDto {

    private Integer unitId;
    private Double totalPrice;
    private String status;

}

