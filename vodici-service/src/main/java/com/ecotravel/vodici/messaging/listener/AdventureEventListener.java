package com.ecotravel.vodici.messaging.listener;

import com.ecotravel.vodici.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.raflab.shared.events.AdventureCreatedEvent;
import org.raflab.shared.events.AdventureDeletedEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AdventureEventListener {

    private final ObjectMapper objectMapper;

    public AdventureEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.VODICI_ADVENTURE_EVENTS_QUEUE)
    public void onAdventureEvent(Message message) {
        try {
            String typeId = (String) message.getMessageProperties().getHeaders().get("__TypeId__");
            if ("AdventureCreatedEvent".equals(typeId) || "org.raflab.shared.events.AdventureCreatedEvent".equals(typeId)) {
                AdventureCreatedEvent event = objectMapper.readValue(message.getBody(), AdventureCreatedEvent.class);
                System.out.println(">>> RABBITMQ CONSUME: AdventureCreatedEvent primljen - id=" + event.getAdventureId() + ", title=" + event.getTitle());
            } else if ("AdventureDeletedEvent".equals(typeId) || "org.raflab.shared.events.AdventureDeletedEvent".equals(typeId)) {
                AdventureDeletedEvent event = objectMapper.readValue(message.getBody(), AdventureDeletedEvent.class);
                System.out.println(">>> RABBITMQ CONSUME: AdventureDeletedEvent primljen - id=" + event.getAdventureId() + ", title=" + event.getTitle());
            }
        } catch (Exception e) {
            System.out.println(">>> RABBITMQ ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
