package org.raflab.shared.events;

import java.time.LocalDateTime;

public class TransactionCreatedEvent {

    private Long transactionId;
    private Long bookingId;
    private String invoiceNumber;
    private Double amount;
    private String paymentMethodName;
    private LocalDateTime occurredAt;

    public TransactionCreatedEvent() {
    }

    public TransactionCreatedEvent(Long transactionId, Long bookingId, String invoiceNumber,
                                   Double amount, String paymentMethodName, LocalDateTime occurredAt) {
        this.transactionId = transactionId;
        this.bookingId = bookingId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.paymentMethodName = paymentMethodName;
        this.occurredAt = occurredAt;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
