package com.ecotravel.vodici.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Zahtev za kreiranje vodica")
public class GuideRequestDTO {

    @NotBlank(message = "Ime vodica je obavezno")
    @Schema(description = "Ime vodica", example = "Marko")
    private String firstName;

    @NotBlank(message = "Prezime vodica je obavezno")
    @Schema(description = "Prezime vodica", example = "Markovic")
    private String lastName;

    @Schema(description = "Biografija vodica")
    private String bio;

    @DecimalMin(value = "1.0", message = "Minimalni rejting je 1.0")
    @DecimalMax(value = "5.0", message = "Maksimalni rejting je 5.0")
    @Schema(description = "Rejting vodica od 1.0 do 5.0", example = "4.5")
    private Double rating;
}
