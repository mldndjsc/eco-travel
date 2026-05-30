package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.GuideExternalDTO;

import java.util.List;

public class ExpertGuideStrategy implements GuideSelectionStrategy {

    private static final double MIN_RATING = 4.5;

    @Override
    public List<GuideExternalDTO> selectGuides(List<GuideExternalDTO> candidates) {
        return candidates.stream()
                .filter(g -> g.getRating() != null && g.getRating() >= MIN_RATING)
                .filter(g -> g.getValidCertificationsCount() != null && g.getValidCertificationsCount() > 0)
                .toList();
    }

    @Override
    public String getStrategyDescription() {
        return "Ekspertski vodic: rejting >= 4.5 i barem 1 vazeci sertifikat (za HARD/EXTREME avanture)";
    }
}
