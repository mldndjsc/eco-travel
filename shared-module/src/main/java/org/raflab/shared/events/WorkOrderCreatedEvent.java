package org.raflab.shared.events;
import java.time.LocalDateTime;

public class WorkOrderCreatedEvent {
    private Long unitId;
    private Long workOrderId;
    private LocalDateTime scheduledFor;
    private Integer durationHours;

    public WorkOrderCreatedEvent() {}
    public WorkOrderCreatedEvent(Long unitId, Long workOrderId, LocalDateTime scheduledFor, Integer durationHours) {
        this.unitId = unitId; this.workOrderId = workOrderId; this.scheduledFor = scheduledFor; this.durationHours = durationHours;
    }
    public Long getUnitId() { return unitId; } public void setUnitId(Long unitId) { this.unitId = unitId; }
    public Long getWorkOrderId() { return workOrderId; } public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    public LocalDateTime getScheduledFor() { return scheduledFor; } public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }
    public Integer getDurationHours() { return durationHours; } public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }
}