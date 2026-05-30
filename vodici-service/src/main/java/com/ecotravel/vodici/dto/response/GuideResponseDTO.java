package com.ecotravel.vodici.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Odgovor sa podacima vodica")
public class GuideResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private Double rating;
    private List<LanguageResponseDTO> languages;
    private Integer validCertificationsCount;
}
