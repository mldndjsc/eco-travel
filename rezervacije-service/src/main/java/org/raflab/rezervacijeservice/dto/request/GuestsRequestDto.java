package org.raflab.rezervacijeservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GuestsRequestDto {

    @NotEmpty(message = "Morate uneti ime osobe")
    private String firstName;
    @NotEmpty(message = "Morate uneti prezime osobe")
    private String lastName;
    @NotEmpty(message = "Morate uneti email osobe/gosta")
    private String email;
}
