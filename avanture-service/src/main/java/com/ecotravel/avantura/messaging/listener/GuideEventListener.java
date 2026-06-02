package com.ecotravel.avantura.messaging.listener;

import com.ecotravel.avantura.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.raflab.shared.events.GuideCreatedEvent;
import org.raflab.shared.events.GuideDeletedEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GuideEventListener {

    private final ObjectMapper objectMapper;

    public GuideEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.AVANTURE_GUIDE_EVENTS_QUEUE)
    public void onGuideEvent(Message message) {
        try {
            String typeId = (String) message.getMessageProperties().getHeaders().get("__TypeId__");
            if ("GuideCreatedEvent".equals(typeId) || "org.raflab.shared.events.GuideCreatedEvent".equals(typeId)) {
                GuideCreatedEvent event = objectMapper.readValue(message.getBody(), GuideCreatedEvent.class);
                System.out.println(">>> RABBITMQ CONSUME: GuideCreatedEvent primljen - id=" + event.getGuideId() + ", name=" + event.getFirstName() + " " + event.getLastName());
            } else if ("GuideDeletedEvent".equals(typeId) || "org.raflab.shared.events.GuideDeletedEvent".equals(typeId)) {
                GuideDeletedEvent event = objectMapper.readValue(message.getBody(), GuideDeletedEvent.class);
                System.out.println(">>> RABBITMQ CONSUME: GuideDeletedEvent primljen - id=" + event.getGuideId() + ", name=" + event.getFirstName() + " " + event.getLastName());
            }
        } catch (Exception e) {
            System.out.println(">>> RABBITMQ ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
