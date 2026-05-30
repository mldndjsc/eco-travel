package com.ecotravel.avantura.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LanguageExternalDTO {
    private Long id;
    private String languageName;
    private String languageCode;
}
