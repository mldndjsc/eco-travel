package com.ecotravel.avantura.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Zahtev za kreiranje kategorije avanture")
public class AdventureCategoryRequestDTO {

    @NotBlank(message = "Naziv kategorije je obavezan")
    @Schema(description = "Naziv kategorije", example = "Planinarenje")
    private String name;

    @Schema(description = "Opis kategorije", example = "Avanture u prirodi")
    private String description;
}
