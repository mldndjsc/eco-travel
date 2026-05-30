package com.ecotravel.avantura.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GuideExternalDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private Double rating;
    private List<LanguageExternalDTO> languages;
    private Integer validCertificationsCount;
}
