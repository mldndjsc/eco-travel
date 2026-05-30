package com.ecotravel.vodici.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Zahtev za dodavanje sertifikata")
public class CertificationRequestDTO {

    @NotBlank(message = "Naziv sertifikata je obavezan")
    @Schema(description = "Naziv sertifikata", example = "Planinarski vodic - nivo 2")
    private String name;

    @Schema(description = "Institucija koja je izdala sertifikat", example = "Planinski savez Srbije")
    private String issuingBody;

    @NotNull(message = "Datum isteka je obavezan")
    @Schema(description = "Datum isteka sertifikata", example = "2027-12-31")
    private LocalDate expiryDate;
}
