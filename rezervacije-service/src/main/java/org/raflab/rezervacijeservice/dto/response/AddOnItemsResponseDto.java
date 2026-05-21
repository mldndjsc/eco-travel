package org.raflab.rezervacijeservice.dto.response;

import lombok.Data;
import org.raflab.rezervacijeservice.model.Bookings;

@Data
public class AddOnItemsResponseDto {

    private BookingsResponseDto bookingsResponseDto;
    private String serviceName;
    private Double price;
}
