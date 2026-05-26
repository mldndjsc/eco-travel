package org.raflab.finansijeservice.messaging.listener;

import lombok.RequiredArgsConstructor;
import org.raflab.finansijeservice.config.RabbitMQConfig;
import org.raflab.finansijeservice.service.InvoicesService;
import org.raflab.shared.events.ReservationPaymentRecordedFeedbackEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationFeedbackListener {

    private final InvoicesService invoicesService;

    @RabbitListener(queues = RabbitMQConfig.FINANCE_RESERVATION_FEEDBACK_QUEUE)
    public void onPaymentRecordedFeedback(ReservationPaymentRecordedFeedbackEvent event) {
        System.out.println("RABBITMQ FEEDBACK CONSUME: ReservationPaymentRecordedFeedbackEvent primljen za bookingId="
                + event.getBookingId() + ", transactionId=" + event.getTransactionId());

        invoicesService.markReservationFeedbackReceived(event.getInvoiceNumber());

        System.out.println("RABBITMQ FEEDBACK ACK: ReservationPaymentRecordedFeedbackEvent obradjen za invoiceNumber="
                + event.getInvoiceNumber());
    }
}
