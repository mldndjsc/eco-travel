package org.raflab.rezervacijeservice.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreatedEvent {
    private Long transactionId;
    private Long bookingId;
    private String invoiceNumber;
    private Double amount;
    private String paymentMethodName;
    private LocalDateTime occurredAt;
}
