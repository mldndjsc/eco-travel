package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.GuideExternalDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GuideListFallbackCreator extends FallbackResponseCreator<List<GuideExternalDTO>> {

    @Override
    public List<GuideExternalDTO> createFallbackResponse() {
        return Collections.emptyList();
    }
}
