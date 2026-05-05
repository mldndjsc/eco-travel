package org.raflab.finansijeservice.converter;

import org.raflab.finansijeservice.dto.request.InvoicesRequestDto;
import org.raflab.finansijeservice.dto.response.InvoicesResponseDto;
import org.raflab.finansijeservice.model.Invoices;

import java.util.ArrayList;
import java.util.List;

public class InvoicesConverter {

    public static Invoices toInvoices(InvoicesRequestDto invoicesRequestDto) {
        Invoices invoices = new Invoices();
        invoices.setInvoiceNumber(invoicesRequestDto.getInvoiceNumber());
        invoices.setBookingId(invoicesRequestDto.getBookingId());
        invoices.setSubtotal(invoicesRequestDto.getSubtotal());
        invoices.setStatus(invoicesRequestDto.getStatus());

        return invoices;
    }

    public static Invoices updateToInvoices(InvoicesRequestDto invoicesRequestDto,Long id) {
        Invoices invoices = new Invoices();
        invoices.setId(id);
        invoices.setInvoiceNumber(invoicesRequestDto.getInvoiceNumber());
        invoices.setBookingId(invoicesRequestDto.getBookingId());
        invoices.setSubtotal(invoicesRequestDto.getSubtotal());
        invoices.setStatus(invoicesRequestDto.getStatus());


        return invoices;
    }

    public static InvoicesResponseDto toInvoicesResponse(Invoices invoices) {
        InvoicesResponseDto invoicesResponseDto = new InvoicesResponseDto();
        invoicesResponseDto.setId(invoices.getId());
        invoicesResponseDto.setInvoiceNumber(invoices.getInvoiceNumber());
        invoicesResponseDto.setStatus(invoices.getStatus());
        invoicesResponseDto.setBookingId(invoices.getBookingId());
        invoicesResponseDto.setSubtotal(invoices.getSubtotal());
        invoicesResponseDto.setTotal_amount(invoices.getTotal_amount());
        invoicesResponseDto.setTaxRatesResponseDto(TaxRatesConverter.toTaxRatesResponseDto(invoices.getTaxRates()));

        return invoicesResponseDto;
    }

    public static List<InvoicesResponseDto> toInvoicesResponseList(List<Invoices> invoicesList) {
        List<InvoicesResponseDto> invoicesResponseList = new ArrayList<>();
        for (Invoices invoices : invoicesList) {
            invoicesResponseList.add(toInvoicesResponse(invoices));
        }
        return invoicesResponseList;
    }
}
