package com.ecotravel.avantura.dto.request;

import com.ecotravel.avantura.entity.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Zahtev za kreiranje avanture")
public class AdventureRequestDTO {

    @NotBlank(message = "Naziv avanture je obavezan")
    @Schema(description = "Naziv avanture", example = "Planinarenje na Kopaoniku")
    private String title;

    @Schema(description = "Opis avanture")
    private String description;

    @NotNull(message = "Tezina je obavezna")
    @Schema(description = "Tezina avanture", example = "MEDIUM")
    private Difficulty difficulty;

    @NotNull(message = "Osnovna cena je obavezna")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti pozitivna")
    @Schema(description = "Osnovna cena avanture", example = "5000.00")
    private BigDecimal basePrice;

    @Schema(description = "ID kategorije", example = "1")
    private Long categoryId;
}
