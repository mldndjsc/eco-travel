package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.GuideExternalDTO;
import org.springframework.stereotype.Component;

@Component
public class GuideFallbackCreator extends FallbackResponseCreator<GuideExternalDTO> {

    @Override
    public GuideExternalDTO createFallbackResponse() {
        GuideExternalDTO fallback = new GuideExternalDTO();
        fallback.setId(null);
        fallback.setFirstName("Nedostupan");
        fallback.setLastName("Vodic");
        fallback.setBio("Podaci o vodicu trenutno nisu dostupni");
        fallback.setRating(null);
        fallback.setValidCertificationsCount(0);
        return fallback;
    }
}
