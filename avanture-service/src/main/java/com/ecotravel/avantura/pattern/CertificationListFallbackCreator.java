package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.CertificationExternalDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CertificationListFallbackCreator extends FallbackResponseCreator<List<CertificationExternalDTO>> {

    @Override
    public List<CertificationExternalDTO> createFallbackResponse() {
        return Collections.emptyList();
    }
}
