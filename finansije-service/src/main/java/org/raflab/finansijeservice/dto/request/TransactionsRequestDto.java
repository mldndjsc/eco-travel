package org.raflab.finansijeservice.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.finansijeservice.model.Invoices;
import org.raflab.finansijeservice.model.PaymentMethods;


import java.time.LocalDateTime;

@Data
public class TransactionsRequestDto {

    @NotNull(message = "Morate uneti iznos transakcije")
    private Double amount;
    @NotNull(message = "Morate uneti metodu placanja")
    private PaymentMethodsRequestDto paymentMethodsRequestDto;
    @NotNull(message = "Morate uneti na koji racun se odnosi transakcija")
    private InvoicesRequestDto invoiceRequestDto;
}
