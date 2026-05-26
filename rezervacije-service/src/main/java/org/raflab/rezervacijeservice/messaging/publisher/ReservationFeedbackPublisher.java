package org.raflab.rezervacijeservice.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.config.RabbitMQConfig;
import org.raflab.shared.events.ReservationPaymentRecordedFeedbackEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationFeedbackPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentRecordedFeedback(ReservationPaymentRecordedFeedbackEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.FEEDBACK_EXCHANGE,
                    RabbitMQConfig.PAYMENT_RECORDED_FEEDBACK_ROUTING_KEY,
                    event
            );
            System.out.println("RABBITMQ FEEDBACK PUBLISH: ReservationPaymentRecordedFeedbackEvent poslat za bookingId="
                    + event.getBookingId() + ", transactionId=" + event.getTransactionId()
                    + ", exchange=" + RabbitMQConfig.FEEDBACK_EXCHANGE
                    + ", routingKey=" + RabbitMQConfig.PAYMENT_RECORDED_FEEDBACK_ROUTING_KEY);
        } catch (AmqpException ex) {
            System.err.println("RABBITMQ FEEDBACK PUBLISH ERROR: Rezervacija je azurirana lokalno, ali feedback nije poslat. Razlog: "
                    + ex.getMessage());
        }
    }
}
