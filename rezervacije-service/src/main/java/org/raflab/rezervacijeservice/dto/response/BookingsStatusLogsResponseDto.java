package org.raflab.rezervacijeservice.dto.response;

import lombok.Data;
import org.raflab.rezervacijeservice.model.Bookings;


import java.time.LocalDateTime;

@Data
public class BookingsStatusLogsResponseDto {

    private BookingsResponseDto bookingsResponseDto;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;
}
