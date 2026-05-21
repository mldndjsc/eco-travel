package org.raflab.finansijeservice.service;

import org.raflab.finansijeservice.converter.PaymentMethodsConverter;
import org.raflab.finansijeservice.converter.TaxRatesConverter;
import org.raflab.finansijeservice.dto.request.PaymentMethodsRequestDto;
import org.raflab.finansijeservice.dto.request.TaxRatesRequestDto;
import org.raflab.finansijeservice.dto.response.PaymentMethodsResponseDto;
import org.raflab.finansijeservice.dto.response.TaxRatesResponseDto;
import org.raflab.finansijeservice.model.TaxRates;
import org.raflab.finansijeservice.repository.PaymentMethodsRepository;
import org.raflab.finansijeservice.repository.TaxRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxRatesService {

    @Autowired
    TaxRatesRepository taxRatesRepository;

    public void save(TaxRatesRequestDto taxRatesRequestDto){
        taxRatesRepository.save(TaxRatesConverter.toTaxRates(taxRatesRequestDto));
    }

    public void updateById(TaxRatesRequestDto taxRatesRequestDto,Long id){

        if(taxRatesRepository.existsById(id)){

            TaxRates taxRates = taxRatesRepository.findById(id).get();
            taxRates.setTaxName(taxRatesRequestDto.getTaxName());
            taxRates.setPercentage(taxRatesRequestDto.getPercentage());
            taxRatesRepository.save(taxRates);
        }else{
            taxRatesRepository.save(TaxRatesConverter.toTaxRates(taxRatesRequestDto));
        }
    }

    public List<TaxRatesResponseDto> findAll(){
        return TaxRatesConverter.toTaxRatesResponseList(taxRatesRepository.findAll());
    }
    public TaxRatesResponseDto findById(Long id){
        return TaxRatesConverter.toTaxRatesResponseDto(taxRatesRepository.findById(id).get());
    }
    public void deleteById( Long id) {
        taxRatesRepository.deleteById(id);
    }
}
