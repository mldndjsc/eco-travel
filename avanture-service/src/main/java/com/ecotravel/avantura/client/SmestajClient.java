package com.ecotravel.avantura.client;

import com.ecotravel.avantura.config.FeignConfig;
import com.ecotravel.avantura.dto.external.UnitExternalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "smestaj-client", url = "${smestaj-service.url}", configuration = FeignConfig.class)
public interface SmestajClient {

    @GetMapping("/api/units")
    List<UnitExternalDTO> getUnits(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "unitType", required = false) String unitType,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice
    );
}
