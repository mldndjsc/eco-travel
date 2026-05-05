package org.raflab.rezervacijeservice.observer;

public interface BookingSubscriber {
    void update(Long bookingId, String oldStatus, String newStatus);
}
