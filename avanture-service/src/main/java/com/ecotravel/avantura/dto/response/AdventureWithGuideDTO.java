package com.ecotravel.avantura.dto.response;

import com.ecotravel.avantura.dto.external.CertificationExternalDTO;
import com.ecotravel.avantura.dto.external.GuideExternalDTO;
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
@Schema(description = "Avantura sa podacima vodica")
public class AdventureWithGuideDTO {
    private AdventureResponseDTO adventure;
    private GuideExternalDTO guide;
    private List<CertificationExternalDTO> guideCertifications;
    private boolean guideEligible;
}
