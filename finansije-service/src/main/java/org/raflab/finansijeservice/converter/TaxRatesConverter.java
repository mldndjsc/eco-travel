package org.raflab.finansijeservice.converter;

import org.raflab.finansijeservice.dto.request.TaxRatesRequestDto;
import org.raflab.finansijeservice.dto.response.TaxRatesResponseDto;
import org.raflab.finansijeservice.model.TaxRates;

import java.util.ArrayList;
import java.util.List;

public class TaxRatesConverter {

    public static TaxRates toTaxRates(TaxRatesRequestDto taxRatesRequestDto) {
        TaxRates taxRates = new TaxRates();
        taxRates.setTaxName(taxRatesRequestDto.getTaxName());
        taxRates.setPercentage(taxRatesRequestDto.getPercentage());


        return taxRates;
    }

    public static TaxRates updateToTaxRates(TaxRatesRequestDto taxRatesRequestDto,Long id) {
        TaxRates taxRates = new TaxRates();
        taxRates.setId(id);
        taxRates.setTaxName(taxRatesRequestDto.getTaxName());
        taxRates.setPercentage(taxRatesRequestDto.getPercentage());


        return taxRates;
    }
    public static TaxRatesResponseDto toTaxRatesResponseDto(TaxRates taxRates) {
        TaxRatesResponseDto taxRatesResponseDto = new TaxRatesResponseDto();
        taxRatesResponseDto.setId(taxRates.getId());
        taxRatesResponseDto.setTaxName(taxRates.getTaxName());
        taxRatesResponseDto.setPercentage(taxRates.getPercentage());


        return taxRatesResponseDto;
    }

    public static List<TaxRatesResponseDto> toTaxRatesResponseList(List<TaxRates> taxRatesList) {
        List<TaxRatesResponseDto> taxRatesResponseDtoList = new ArrayList<>();
        for (TaxRates taxRates : taxRatesList) {
            taxRatesResponseDtoList.add(toTaxRatesResponseDto(taxRates));
        }
        return taxRatesResponseDtoList;
    }
}
