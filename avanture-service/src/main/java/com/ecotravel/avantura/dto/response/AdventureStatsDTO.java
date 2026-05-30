package com.ecotravel.avantura.dto.response;

import com.ecotravel.avantura.entity.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Statistika avanture")
public class AdventureStatsDTO {
    private Long id;
    private String title;
    private Difficulty difficulty;
    private BigDecimal basePrice;
    private Double averageRating;
    private Long reviewCount;

    public AdventureStatsDTO(Long id, String title, Difficulty difficulty, BigDecimal basePrice,
                              Double averageRating, Long reviewCount) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.basePrice = basePrice;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }
}
