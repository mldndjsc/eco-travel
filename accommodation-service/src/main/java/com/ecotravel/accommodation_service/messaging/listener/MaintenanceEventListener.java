package com.ecotravel.accommodation_service.messaging.listener;

import com.ecotravel.accommodation_service.config.RabbitMQConfig;
import com.ecotravel.accommodation_service.service.AvailabilityService;
import org.raflab.shared.events.WorkOrderCompletedEvent;
import org.raflab.shared.events.WorkOrderCreatedEvent;
import org.raflab.shared.events.WorkOrderDeletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMQConfig.ACCOMMODATION_MAINTENANCE_QUEUE) // OVO IDE SAMO OVDE, NA KLASU!
public class MaintenanceEventListener {

    private final AvailabilityService availabilityService;

    public MaintenanceEventListener(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @RabbitHandler // OVO IDE NA METODE!
    public void handleWorkOrderCreated(WorkOrderCreatedEvent event) {
        System.out.println(">>> RABBITMQ CONSUME (Accommodation): Primljen zahtev za kreiranje održavanja (WorkOrderId: " + event.getWorkOrderId() + ")");
        availabilityService.blockUnitForMaintenance(event.getUnitId(), event.getWorkOrderId(), event.getScheduledFor(), event.getDurationHours());
    }

    @RabbitHandler // OVO IDE NA METODE!
    public void handleWorkOrderCompleted(WorkOrderCompletedEvent event) {
        System.out.println(">>> RABBITMQ CONSUME (Accommodation): Održavanje završeno, oslobađam termin (WorkOrderId: " + event.getWorkOrderId() + ")");
        availabilityService.unblockUnitFromMaintenance(event.getUnitId(), event.getWorkOrderId());
    }

    @RabbitHandler // OVO IDE NA METODE!
    public void handleWorkOrderDeleted(WorkOrderDeletedEvent event) {
        System.out.println(">>> RABBITMQ CONSUME (Accommodation): Održavanje otkazano/obrisano, oslobađam termin (WorkOrderId: " + event.getWorkOrderId() + ")");
        availabilityService.unblockUnitFromMaintenance(event.getUnitId(), event.getWorkOrderId());
    }
}