package com.ecotravel.maintenance_service.service;

import com.ecotravel.maintenance_service.client.AccommodationClientFacade;
import com.ecotravel.maintenance_service.client.dto.UnitResponseDTO;
import com.ecotravel.maintenance_service.dto.WorkOrderRequestDTO;
import com.ecotravel.maintenance_service.dto.WorkOrderResponseDTO;
import com.ecotravel.maintenance_service.mapper.MaintenanceMapper;
import com.ecotravel.maintenance_service.messaging.publisher.MaintenanceFeedbackPublisher;
import com.ecotravel.maintenance_service.model.*;
import com.ecotravel.maintenance_service.repository.MaintenanceLogRepository;
import com.ecotravel.maintenance_service.repository.MaintenanceTaskRepository;
import com.ecotravel.maintenance_service.repository.WorkOrderRepository;
import com.ecotravel.maintenance_service.repository.WorkerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.raflab.shared.events.BookingCreatedMaintenanceEvent;
import org.raflab.shared.events.MaintenanceWorkOrderCreatedFeedbackEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkerRepository workerRepository;
    private final MaintenanceTaskRepository taskRepository;
    private final MaintenanceLogRepository logRepository;
    private final MaintenanceMapper mapper;
    private final AccommodationClientFacade accommodationClientFacade;
    private final MaintenanceFeedbackPublisher maintenanceFeedbackPublisher;

    public WorkOrderService(WorkOrderRepository workOrderRepository, WorkerRepository workerRepository,
                            MaintenanceTaskRepository taskRepository, MaintenanceLogRepository logRepository,
                            MaintenanceMapper mapper, AccommodationClientFacade accommodationClientFacade,
                            MaintenanceFeedbackPublisher maintenanceFeedbackPublisher) {
        this.workOrderRepository = workOrderRepository;
        this.workerRepository = workerRepository;
        this.taskRepository = taskRepository;
        this.logRepository = logRepository;
        this.mapper = mapper;
        this.accommodationClientFacade = accommodationClientFacade;
        this.maintenanceFeedbackPublisher = maintenanceFeedbackPublisher;
    }
    public WorkOrderResponseDTO getWorkOrderById(Long id) {
        WorkOrder order = workOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nalog sa ID " + id + " nije pronađen"));
        return mapper.toResponseDTO(order);
    }
    @Transactional
    public WorkOrderResponseDTO createWorkOrder(WorkOrderRequestDTO dto) {

        UnitResponseDTO unit = accommodationClientFacade.getUnit(dto.getUnitId());
        Boolean isAvailable = accommodationClientFacade.checkAvailability(
                dto.getUnitId(),
                dto.getScheduledFor().toLocalDate(),
                dto.getScheduledFor().toLocalDate()
        );
        if (!isAvailable) {
            throw new IllegalStateException("Nije moguće zakazati održavanje. Smeštajna jedinica je zauzeta u tom periodu.");
        }
        Worker worker = workerRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> new EntityNotFoundException("Radnik nije pronađen"));

        MaintenanceTask task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException("Zadatak nije pronađen"));
        boolean isDuplicate = workOrderRepository.existsDuplicate(
                dto.getUnitId(),
                dto.getWorkerId(),
                dto.getScheduledFor()
        );

        if (isDuplicate) {
            throw new IllegalStateException("Radnik već ima zakazan radni nalog za ovaj smeštaj na izabrani datum!");
        }
        WorkOrder order = new WorkOrder();
        order.setUnitId(dto.getUnitId());
        order.setWorker(worker);
        order.setTask(task);
        order.setScheduledFor(dto.getScheduledFor());
        order.setStatus(WorkOrderStatus.PENDING);

        WorkOrder saved = workOrderRepository.save(order);
        return mapper.toResponseDTO(saved);
    }

    public List<WorkOrderResponseDTO> getWorkOrders(WorkOrderStatus status) {
        List<WorkOrder> orders;
        if (status != null) {
            orders = workOrderRepository.findByStatus(status);
        } else {
            orders = workOrderRepository.findAll();
        }
        return orders.stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public WorkOrderResponseDTO completeWorkOrder(Long orderId, String notes) {
        WorkOrder order = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Nalog nije pronađen"));

        if (order.getStatus() == WorkOrderStatus.COMPLETED) {
            throw new IllegalStateException("Nalog je već završen.");
        }

        order.setStatus(WorkOrderStatus.COMPLETED);
        order.setCompletionDate(LocalDateTime.now());
        WorkOrder updated = workOrderRepository.save(order);

        // Automatsko kreiranje loga kada se nalog završi
        MaintenanceLog log = new MaintenanceLog();
        log.setUnitId(order.getUnitId());
        log.setTaskDescription(order.getTask().getTaskName());
        log.setWorkerName(order.getWorker().getFirstName() + " " + order.getWorker().getLastName());
        log.setCompletedAt(order.getCompletionDate());
        log.setNotes(notes);
        logRepository.save(log);

        return mapper.toResponseDTO(updated);
    }
    public Integer calculateRemainingWorkloadForWorker(Long workerId) {
        if (!workerRepository.existsById(workerId)) {
            throw new EntityNotFoundException("Radnik nije pronađen");
        }

        List<WorkOrderStatus> activeStatuses = List.of(WorkOrderStatus.PENDING, WorkOrderStatus.IN_PROGRESS);
        List<WorkOrder> activeOrders = workOrderRepository.findByWorkerIdAndStatusIn(workerId, activeStatuses);

        return activeOrders.stream()
                .map(order -> order.getTask().getEstimatedDurationHours())
                .reduce(0, Integer::sum); // Matematičko sumiranje sati
    }

    @Transactional
    public void deleteWorkOrder(Long id) {
        if (!workOrderRepository.existsById(id)) {
            throw new EntityNotFoundException("Nalog sa ID " + id + " nije pronađen.");
        }
        workOrderRepository.deleteById(id);
    }

    @Transactional
    public void createCleaningWorkOrderFromBooking(BookingCreatedMaintenanceEvent event) {
        Long cleaningWorkerId = 3L;
        Long cleaningTaskId = 2L;

        LocalDateTime scheduledFor = event.getEndDate().atTime(11, 0);

        boolean duplicateExists = workOrderRepository.existsDuplicate(
                event.getUnitId(),
                cleaningWorkerId,
                scheduledFor
        );

        if (duplicateExists) {
            System.out.println("Maintenance work order vec postoji za unitId="
                    + event.getUnitId()
                    + ", workerId=" + cleaningWorkerId
                    + ", scheduledFor=" + scheduledFor);

            maintenanceFeedbackPublisher.publishWorkOrderCreatedFeedback(
                    new MaintenanceWorkOrderCreatedFeedbackEvent(
                            event.getBookingId(),
                            event.getUnitId(),
                            null,
                            "ALREADY_EXISTS",
                            scheduledFor,
                            LocalDateTime.now(),
                            "Maintenance servis je prepoznao da work order vec postoji za dati booking."
                    )
            );
            return;
        }

        Worker worker = workerRepository.findById(cleaningWorkerId)
                .orElseThrow(() -> new EntityNotFoundException("Radnik za ciscenje nije pronadjen"));

        MaintenanceTask task = taskRepository.findById(cleaningTaskId)
                .orElseThrow(() -> new EntityNotFoundException("Task za generalno ciscenje nije pronadjen"));

        WorkOrder order = new WorkOrder();
        order.setUnitId(event.getUnitId());
        order.setWorker(worker);
        order.setTask(task);
        order.setScheduledFor(scheduledFor);
        order.setStatus(WorkOrderStatus.PENDING);

        WorkOrder savedOrder = workOrderRepository.save(order);

        System.out.println("Maintenance work order kreiran asinhrono za bookingId="
                + event.getBookingId()
                + ", unitId=" + event.getUnitId()
                + ", scheduledFor=" + scheduledFor);

        maintenanceFeedbackPublisher.publishWorkOrderCreatedFeedback(
                new MaintenanceWorkOrderCreatedFeedbackEvent(
                        event.getBookingId(),
                        event.getUnitId(),
                        savedOrder.getId(),
                        savedOrder.getStatus().name(),
                        savedOrder.getScheduledFor(),
                        LocalDateTime.now(),
                        "Maintenance servis je kreirao work order za zavrsno ciscenje."
                )
        );
    }
}