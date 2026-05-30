package com.ecotravel.avantura.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Zahtev za dodavanje recenzije")
public class AdventureReviewRequestDTO {

    @NotNull(message = "Ocena je obavezna")
    @Min(value = 1, message = "Minimalna ocena je 1")
    @Max(value = 5, message = "Maksimalna ocena je 5")
    @Schema(description = "Ocena od 1 do 5", example = "5")
    private Integer rating;

    @Schema(description = "Komentar recenzije")
    private String comment;

    @Schema(description = "Ime recenzenta", example = "Marko Markovic")
    private String reviewerName;
}
