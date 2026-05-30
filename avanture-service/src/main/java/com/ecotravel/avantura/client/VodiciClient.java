package com.ecotravel.avantura.client;

import com.ecotravel.avantura.config.FeignConfig;
import com.ecotravel.avantura.dto.external.CertificationExternalDTO;
import com.ecotravel.avantura.dto.external.GuideExternalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "vodici-client", url = "${vodici-service.url}", configuration = FeignConfig.class)
public interface VodiciClient {

    @GetMapping("/api/guides/{id}")
    GuideExternalDTO getGuideById(@PathVariable("id") Long id);

    @GetMapping("/api/guides")
    List<GuideExternalDTO> getGuides(
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "languageId", required = false) Long languageId
    );

    @GetMapping("/api/guides/{id}/certifications")
    List<CertificationExternalDTO> getGuideCertifications(
            @PathVariable("id") Long guideId,
            @RequestParam(value = "validOnly", defaultValue = "false") boolean validOnly
    );
}
