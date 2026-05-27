package com.ecotravel.maintenance_service.messaging.publisher;

import com.ecotravel.maintenance_service.config.RabbitMQConfig;
import org.raflab.shared.events.MaintenanceWorkOrderCreatedFeedbackEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceFeedbackPublisher {

    private final RabbitTemplate rabbitTemplate;

    public MaintenanceFeedbackPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishWorkOrderCreatedFeedback(MaintenanceWorkOrderCreatedFeedbackEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.MAINTENANCE_FEEDBACK_EXCHANGE,
                    RabbitMQConfig.MAINTENANCE_WORK_ORDER_CREATED_FEEDBACK_ROUTING_KEY,
                    event
            );

            System.out.println("RABBITMQ FEEDBACK PUBLISH: MaintenanceWorkOrderCreatedFeedbackEvent poslat za bookingId="
                    + event.getBookingId()
                    + ", workOrderId=" + event.getWorkOrderId()
                    + ", exchange=" + RabbitMQConfig.MAINTENANCE_FEEDBACK_EXCHANGE
                    + ", routingKey=" + RabbitMQConfig.MAINTENANCE_WORK_ORDER_CREATED_FEEDBACK_ROUTING_KEY);
        } catch (AmqpException ex) {
            System.err.println("RABBITMQ FEEDBACK PUBLISH ERROR: Work order je kreiran lokalno, ali feedback nije poslat. Razlog: "
                    + ex.getMessage());
        }
    }
}