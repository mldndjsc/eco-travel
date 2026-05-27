package org.raflab.finansijeservice.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.raflab.finansijeservice.config.RabbitMQConfig;
import org.raflab.shared.events.TransactionCreatedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishTransactionCreated(TransactionCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.TRANSACTION_CREATED_ROUTING_KEY,
                    event
            );
            System.out.println("RABBITMQ PUBLISH: TransactionCreatedEvent poslat za bookingId="
                    + event.getBookingId() + ", transactionId=" + event.getTransactionId()
                    + ", exchange=" + RabbitMQConfig.EXCHANGE
                    + ", routingKey=" + RabbitMQConfig.TRANSACTION_CREATED_ROUTING_KEY);
        } catch (AmqpException ex) {
            System.err.println("RABBITMQ PUBLISH ERROR: Transakcija je sacuvana lokalno, ali event nije poslat. Razlog: "
                    + ex.getMessage());
        }
    }
}
