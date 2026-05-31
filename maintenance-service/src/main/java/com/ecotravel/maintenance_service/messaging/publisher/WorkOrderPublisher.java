package com.ecotravel.maintenance_service.messaging.publisher;

import com.ecotravel.maintenance_service.config.RabbitMQConfig;
import org.raflab.shared.events.WorkOrderCompletedEvent;
import org.raflab.shared.events.WorkOrderCreatedEvent;
import org.raflab.shared.events.WorkOrderDeletedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderPublisher {

    private final RabbitTemplate rabbitTemplate;
    public static final String ROUTING_KEY_CREATED = "maintenance.work-order.created";
    public static final String ROUTING_KEY_COMPLETED = "maintenance.work-order.completed";
    public static final String ROUTING_KEY_DELETED = "maintenance.work-order.deleted";

    public WorkOrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishWorkOrderCreated(WorkOrderCreatedEvent event) {
        send(ROUTING_KEY_CREATED, event);
    }

    public void publishWorkOrderCompleted(WorkOrderCompletedEvent event) {
        send(ROUTING_KEY_COMPLETED, event);
    }

    public void publishWorkOrderDeleted(WorkOrderDeletedEvent event) {
        send(ROUTING_KEY_DELETED, event);
    }

    private void send(String routingKey, Object event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.MAINTENANCE_EXCHANGE, routingKey, event);
            System.out.println(">>> RABBITMQ PUBLISH: Poslat dogadjaj [" + routingKey + "]");
        } catch (AmqpException ex) {
            System.err.println("Greska pri slanju: " + ex.getMessage());
        }
    }
}