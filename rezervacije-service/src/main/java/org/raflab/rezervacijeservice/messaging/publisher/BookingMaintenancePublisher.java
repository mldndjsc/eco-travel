package org.raflab.rezervacijeservice.messaging.publisher;

import org.raflab.rezervacijeservice.config.RabbitMQConfig;
import org.raflab.shared.events.BookingCreatedMaintenanceEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingMaintenancePublisher {

    private final RabbitTemplate rabbitTemplate;

    public BookingMaintenancePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBookingCreated(BookingCreatedMaintenanceEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MAINTENANCE_EXCHANGE,
                RabbitMQConfig.BOOKING_CREATED_MAINTENANCE_ROUTING_KEY,
                event
        );

        System.out.println("RABBITMQ PUBLISH: BookingCreatedMaintenanceEvent poslat za bookingId="
                + event.getBookingId()
                + ", unitId=" + event.getUnitId()
                + ", exchange=" + RabbitMQConfig.MAINTENANCE_EXCHANGE
                + ", routingKey=" + RabbitMQConfig.BOOKING_CREATED_MAINTENANCE_ROUTING_KEY);
    }
}
