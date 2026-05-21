package org.raflab.rezervacijeservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.rezervacijeservice.model.Bookings;


import java.time.LocalDateTime;

@Data
public class BookingStatusLogsRequestDto {

    @NotNull(message = "morate uneti booking na koji se booking status odnosi")
    private BookingsRequestDto bookingsRequestDto;
    @NotEmpty(message = "Morate uneti stari status loga")
    private String oldStatus;
    @NotEmpty(message = "Morate uneti novi status loga")
    private String newStatus;
    @NotNull(message = "Morate uneti vreme loga")
    private LocalDateTime changedAt;
}
