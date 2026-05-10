package org.raflab.finansijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.finansijeservice.dto.request.PaymentMethodsRequestDto;
import org.raflab.finansijeservice.dto.request.TaxRatesRequestDto;
import org.raflab.finansijeservice.dto.response.PaymentMethodsResponseDto;
import org.raflab.finansijeservice.dto.response.TaxRatesResponseDto;
import org.raflab.finansijeservice.model.PaymentMethods;
import org.raflab.finansijeservice.model.TaxRates;
import org.raflab.finansijeservice.service.PaymentMethodsService;
import org.raflab.finansijeservice.service.TaxRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/finansije")
@RefreshScope
public class TaxRatesController {

    @Autowired
    TaxRatesService taxRatesService;

    @PostMapping(path = "/taxrates/add")
    public void addTaxRates(@RequestBody @Valid TaxRatesRequestDto taxRatesRequestDto){
        taxRatesService.save(taxRatesRequestDto);
    }

    @PutMapping(path = "/taxrates/update/{id}")
    public void updateTaxRates(@RequestBody @Valid TaxRatesRequestDto taxRatesRequestDto,@PathVariable Long id){
        taxRatesService.updateById(taxRatesRequestDto,id);
    }

    @GetMapping(path = "/taxrates/{id}")
    public TaxRatesResponseDto findTaxRatesById(@PathVariable Long id){
        return taxRatesService.findById(id);
    }
    @GetMapping(path = "/taxrates/all")
    public List<TaxRatesResponseDto> getAllTaxRates(){
        return taxRatesService.findAll();
    }
    @DeleteMapping(path = "/taxrates/delete/{id}")
    public void deleteTaxRatesById(@PathVariable Long id) {
        taxRatesService.deleteById(id);
    }

}
