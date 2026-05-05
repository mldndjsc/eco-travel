package org.raflab.rezervacijeservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.rezervacijeservice.model.Bookings;

@Data
public class AddOnItemsRequestDto {

    @NotNull(message = "Morate uneti booking na koji se odnosi dodatna usluga")
    private BookingsRequestDto bookingRequestDto;
    @NotEmpty(message = "Morate uneti naziv dodatne usluge")
    private String serviceName;
    @NotNull(message = "Morate uneti cenu dodatne usluge")
    private Double price;
}
