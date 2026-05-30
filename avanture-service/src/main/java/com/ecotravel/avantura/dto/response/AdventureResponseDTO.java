package com.ecotravel.avantura.dto.response;

import com.ecotravel.avantura.entity.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Odgovor sa podacima avanture")
public class AdventureResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private BigDecimal basePrice;
    private AdventureCategoryResponseDTO category;
    private List<GearRequirementResponseDTO> gearRequirements;
    private Double averageRating;
    private Long reviewCount;
}
