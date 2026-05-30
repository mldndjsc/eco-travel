package com.ecotravel.vodici.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Zahtev za kreiranje jezika")
public class LanguageRequestDTO {

    @NotBlank(message = "Naziv jezika je obavezan")
    @Schema(description = "Naziv jezika", example = "Srpski")
    private String languageName;

    @NotBlank(message = "Kod jezika je obavezan")
    @Schema(description = "Kod jezika", example = "SR")
    private String languageCode;
}
