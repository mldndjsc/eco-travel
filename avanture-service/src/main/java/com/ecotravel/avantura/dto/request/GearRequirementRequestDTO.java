package com.ecotravel.avantura.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Zahtev za dodavanje opreme")
public class GearRequirementRequestDTO {

    @NotBlank(message = "Naziv opreme je obavezan")
    @Schema(description = "Naziv opreme", example = "Planinarski stap")
    private String name;

    @Schema(description = "Opis opreme")
    private String description;

    @Schema(description = "Da li je oprema obavezna", example = "true")
    private Boolean isMandatory = true;
}
