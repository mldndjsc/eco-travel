package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.GuideExternalDTO;

import java.util.List;

public class StandardGuideStrategy implements GuideSelectionStrategy {

    private static final double MIN_RATING = 3.0;

    @Override
    public List<GuideExternalDTO> selectGuides(List<GuideExternalDTO> candidates) {
        return candidates.stream()
                .filter(g -> g.getRating() != null && g.getRating() >= MIN_RATING)
                .toList();
    }

    @Override
    public String getStrategyDescription() {
        return "Standardni vodic: rejting >= 3.0 (za EASY/MEDIUM avanture)";
    }
}
