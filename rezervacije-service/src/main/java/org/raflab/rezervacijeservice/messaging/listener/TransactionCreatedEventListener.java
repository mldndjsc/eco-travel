package org.raflab.rezervacijeservice.messaging.listener;

import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.config.RabbitMQConfig;
import org.raflab.rezervacijeservice.messaging.event.ReservationPaymentRecordedFeedbackEvent;
import org.raflab.rezervacijeservice.messaging.event.TransactionCreatedEvent;
import org.raflab.rezervacijeservice.messaging.publisher.ReservationFeedbackPublisher;
import org.raflab.rezervacijeservice.service.BookingsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionCreatedEventListener {

    private final BookingsService bookingsService;
    private final ReservationFeedbackPublisher reservationFeedbackPublisher;

    @RabbitListener(queues = RabbitMQConfig.RESERVATIONS_FINANCE_EVENTS_QUEUE)
    public void onTransactionCreated(TransactionCreatedEvent event) {
        System.out.println("RABBITMQ CONSUME: TransactionCreatedEvent primljen za bookingId="
                + event.getBookingId() + ", transactionId=" + event.getTransactionId());

        bookingsService.markPaymentRecorded(event.getBookingId());
        reservationFeedbackPublisher.publishPaymentRecordedFeedback(new ReservationPaymentRecordedFeedbackEvent(
                event.getTransactionId(),
                event.getBookingId(),
                event.getInvoiceNumber(),
                "PAYMENT_RECORDED",
                LocalDateTime.now(),
                "Rezervacije servis je evidentirao uplatu za booking."
        ));

        System.out.println("RABBITMQ ACK: TransactionCreatedEvent obradjen za bookingId="
                + event.getBookingId());
    }
}
