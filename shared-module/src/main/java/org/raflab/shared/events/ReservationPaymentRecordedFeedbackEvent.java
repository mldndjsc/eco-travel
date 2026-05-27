package org.raflab.shared.events;

import java.time.LocalDateTime;

public class ReservationPaymentRecordedFeedbackEvent {

    private Long transactionId;
    private Long bookingId;
    private String invoiceNumber;
    private String reservationStatus;
    private LocalDateTime processedAt;
    private String message;

    public ReservationPaymentRecordedFeedbackEvent() {
    }

    public ReservationPaymentRecordedFeedbackEvent(Long transactionId, Long bookingId, String invoiceNumber,
                                                   String reservationStatus, LocalDateTime processedAt, String message) {
        this.transactionId = transactionId;
        this.bookingId = bookingId;
        this.invoiceNumber = invoiceNumber;
        this.reservationStatus = reservationStatus;
        this.processedAt = processedAt;
        this.message = message;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
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
