package org.raflab.rezervacijeservice.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPaymentRecordedFeedbackEvent {
    private Long transactionId;
    private Long bookingId;
    private String invoiceNumber;
    private String reservationStatus;
    private LocalDateTime processedAt;
    private String message;
}
