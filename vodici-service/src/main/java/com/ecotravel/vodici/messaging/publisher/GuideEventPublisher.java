package com.ecotravel.vodici.messaging.publisher;

import com.ecotravel.vodici.config.RabbitMQConfig;
import org.raflab.shared.events.GuideCreatedEvent;
import org.raflab.shared.events.GuideDeletedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class GuideEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public GuideEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishGuideCreated(GuideCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.VODICI_EVENTS_EXCHANGE,
                    RabbitMQConfig.GUIDE_CREATED_ROUTING_KEY,
                    event
            );
            System.out.println(">>> RABBITMQ PUBLISH: GuideCreatedEvent poslat - id=" + event.getGuideId() + ", name=" + event.getFirstName() + " " + event.getLastName());
        } catch (AmqpException ex) {
            System.err.println(">>> RABBITMQ PUBLISH ERROR: " + ex.getMessage());
        }
    }

    public void publishGuideDeleted(GuideDeletedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.VODICI_EVENTS_EXCHANGE,
                    RabbitMQConfig.GUIDE_DELETED_ROUTING_KEY,
                    event
            );
            System.out.println(">>> RABBITMQ PUBLISH: GuideDeletedEvent poslat - id=" + event.getGuideId() + ", name=" + event.getFirstName() + " " + event.getLastName());
        } catch (AmqpException ex) {
            System.err.println(">>> RABBITMQ PUBLISH ERROR: " + ex.getMessage());
        }
    }
}
