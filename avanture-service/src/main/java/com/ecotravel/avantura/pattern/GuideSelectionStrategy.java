package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.GuideExternalDTO;

import java.util.List;

public interface GuideSelectionStrategy {
    List<GuideExternalDTO> selectGuides(List<GuideExternalDTO> candidates);
    String getStrategyDescription();
}
