package org.raflab.rezervacijeservice.messaging.listener;

import lombok.RequiredArgsConstructor;
import org.raflab.rezervacijeservice.config.RabbitMQConfig;
import org.raflab.rezervacijeservice.service.BookingsService;
import org.raflab.shared.events.MaintenanceWorkOrderCreatedFeedbackEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceFeedbackListener {

    private final BookingsService bookingsService;

    @RabbitListener(queues = RabbitMQConfig.RESERVATIONS_MAINTENANCE_FEEDBACK_QUEUE)
    public void onWorkOrderCreatedFeedback(MaintenanceWorkOrderCreatedFeedbackEvent event) {
        System.out.println("RABBITMQ FEEDBACK CONSUME: MaintenanceWorkOrderCreatedFeedbackEvent primljen za bookingId="
                + event.getBookingId() + ", workOrderId=" + event.getWorkOrderId());

        bookingsService.recordMaintenanceWorkOrderCreated(event);

        System.out.println("RABBITMQ FEEDBACK ACK: MaintenanceWorkOrderCreatedFeedbackEvent obradjen za bookingId="
                + event.getBookingId());
    }
}