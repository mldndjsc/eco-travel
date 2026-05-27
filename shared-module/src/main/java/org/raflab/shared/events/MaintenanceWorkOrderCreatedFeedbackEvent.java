package org.raflab.shared.events;

import java.time.LocalDateTime;

public class MaintenanceWorkOrderCreatedFeedbackEvent {

    private Long bookingId;
    private Long unitId;
    private Long workOrderId;
    private String workOrderStatus;
    private LocalDateTime scheduledFor;
    private LocalDateTime processedAt;
    private String message;

    public MaintenanceWorkOrderCreatedFeedbackEvent() {
    }

    public MaintenanceWorkOrderCreatedFeedbackEvent(Long bookingId, Long unitId, Long workOrderId,
                                                    String workOrderStatus, LocalDateTime scheduledFor,
                                                    LocalDateTime processedAt, String message) {
        this.bookingId = bookingId;
        this.unitId = unitId;
        this.workOrderId = workOrderId;
        this.workOrderStatus = workOrderStatus;
        this.scheduledFor = scheduledFor;
        this.processedAt = processedAt;
        this.message = message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(String workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public LocalDateTime getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(LocalDateTime scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
