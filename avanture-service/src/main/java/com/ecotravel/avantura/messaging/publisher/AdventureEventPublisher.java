package com.ecotravel.avantura.messaging.publisher;

import com.ecotravel.avantura.config.RabbitMQConfig;
import org.raflab.shared.events.AdventureCreatedEvent;
import org.raflab.shared.events.AdventureDeletedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AdventureEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AdventureEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAdventureCreated(AdventureCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AVANTURE_EVENTS_EXCHANGE,
                    RabbitMQConfig.ADVENTURE_CREATED_ROUTING_KEY,
                    event
            );
            System.out.println(">>> RABBITMQ PUBLISH: AdventureCreatedEvent poslat - id=" + event.getAdventureId() + ", title=" + event.getTitle());
        } catch (AmqpException ex) {
            System.err.println(">>> RABBITMQ PUBLISH ERROR: " + ex.getMessage());
        }
    }

    public void publishAdventureDeleted(AdventureDeletedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AVANTURE_EVENTS_EXCHANGE,
                    RabbitMQConfig.ADVENTURE_DELETED_ROUTING_KEY,
                    event
            );
            System.out.println(">>> RABBITMQ PUBLISH: AdventureDeletedEvent poslat - id=" + event.getAdventureId());
        } catch (AmqpException ex) {
            System.err.println(">>> RABBITMQ PUBLISH ERROR: " + ex.getMessage());
        }
    }
}
