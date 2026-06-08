package org.raflab.rezervacijeservice.feign.config;

import org.raflab.rezervacijeservice.feign.dto.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "finansije-service",
        configuration = FinansijeFeignConfig.class
)
public interface FinansijeClient {

    @GetMapping("/api/finansije/invoices/all")
    List<InvoiceResponse> getAllInvoices();
}
