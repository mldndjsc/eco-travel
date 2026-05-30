package com.ecotravel.avantura.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Zahtev za kreiranje termina")
public class TimeSlotRequestDTO {

    @NotNull(message = "Vreme pocetka je obavezno")
    @Schema(description = "Vreme pocetka termina", example = "2025-07-01T09:00:00")
    private LocalDateTime startTime;

    @NotNull(message = "Vreme zavrsetka je obavezno")
    @Schema(description = "Vreme zavrsetka termina", example = "2025-07-01T17:00:00")
    private LocalDateTime endTime;

    @NotNull(message = "Maksimalan broj ucesnika je obavezan")
    @Min(value = 1, message = "Mora biti bar jedan ucesnik")
    @Schema(description = "Maksimalan broj ucesnika", example = "10")
    private Integer maxParticipants;
}
