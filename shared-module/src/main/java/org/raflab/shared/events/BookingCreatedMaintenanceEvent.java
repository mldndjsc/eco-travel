package org.raflab.shared.events;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingCreatedMaintenanceEvent {

    private Long bookingId;
    private Long unitId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String bookingStatus;
    private LocalDateTime createdAt;

    public BookingCreatedMaintenanceEvent() {
    }

    public BookingCreatedMaintenanceEvent(Long bookingId, Long unitId, LocalDate startDate,
                                          LocalDate endDate, String bookingStatus, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
        this.createdAt = createdAt;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
