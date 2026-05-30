package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.UnitExternalDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UnitListFallbackCreator extends FallbackResponseCreator<List<UnitExternalDTO>> {

    @Override
    public List<UnitExternalDTO> createFallbackResponse() {
        return Collections.emptyList();
    }
}
