package org.raflab.rezervacijeservice.feign.service;

public interface BookingFinanceService {
    Object getInvoicesForBooking(Long bookingId);
}
