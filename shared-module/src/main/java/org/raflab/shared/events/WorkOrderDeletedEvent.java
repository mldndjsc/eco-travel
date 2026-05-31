package org.raflab.shared.events;
public class WorkOrderDeletedEvent {
    private Long unitId;
    private Long workOrderId;

    public WorkOrderDeletedEvent() {}
    public WorkOrderDeletedEvent(Long unitId, Long workOrderId) {
        this.unitId = unitId; this.workOrderId = workOrderId;
    }
    // GETTERI I SETTERI
    public Long getUnitId() { return unitId; } public void setUnitId(Long unitId) { this.unitId = unitId; }
    public Long getWorkOrderId() { return workOrderId; } public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
}