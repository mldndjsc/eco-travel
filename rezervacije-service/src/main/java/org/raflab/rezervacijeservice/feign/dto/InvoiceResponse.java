package org.raflab.rezervacijeservice.feign.dto;

public record InvoiceResponse(
        Long id,
        String invoiceNumber,
        Double subtotal,
        Double total_amount,
        String status,
        Long bookingId
) {}
