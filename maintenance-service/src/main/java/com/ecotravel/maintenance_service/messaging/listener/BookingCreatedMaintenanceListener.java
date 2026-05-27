package com.ecotravel.maintenance_service.messaging.listener;

import com.ecotravel.maintenance_service.config.RabbitMQConfig;
import com.ecotravel.maintenance_service.service.WorkOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.raflab.shared.events.BookingCreatedMaintenanceEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookingCreatedMaintenanceListener {

    private final WorkOrderService workOrderService;
    private final ObjectMapper objectMapper;

    public BookingCreatedMaintenanceListener(WorkOrderService workOrderService,
                                             ObjectMapper objectMapper) {
        this.workOrderService = workOrderService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.MAINTENANCE_BOOKING_QUEUE)
    public void handleBookingCreated(Message message) {
        try {
            BookingCreatedMaintenanceEvent event =
                    objectMapper.readValue(message.getBody(), BookingCreatedMaintenanceEvent.class);

            System.out.println("RABBITMQ CONSUME: Primljen BookingCreatedMaintenanceEvent: " + event);

            workOrderService.createCleaningWorkOrderFromBooking(event);

            System.out.println("RABBITMQ ACK: Kreiran maintenance work order za bookingId="
                    + event.getBookingId()
                    + ", unitId=" + event.getUnitId());
        } catch (Exception e) {
            System.out.println("RABBITMQ ERROR: Greska pri obradi BookingCreatedMaintenanceEvent: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
