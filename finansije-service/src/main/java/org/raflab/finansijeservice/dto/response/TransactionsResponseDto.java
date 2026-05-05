package org.raflab.finansijeservice.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.raflab.finansijeservice.model.Invoices;
import org.raflab.finansijeservice.model.PaymentMethods;

import java.time.LocalDateTime;

@Data
public class TransactionsResponseDto {

    private Double amount;
    private LocalDateTime date;
    private PaymentMethodsResponseDto paymentMethodResponseDto;
    private InvoicesResponseDto invoiceResponseDto;
}
