package com.ecotravel.avantura.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Odgovor sa podacima kategorije avanture")
public class AdventureCategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
}
