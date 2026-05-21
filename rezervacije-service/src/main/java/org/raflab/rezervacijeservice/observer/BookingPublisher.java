package org.raflab.rezervacijeservice.observer;

public interface BookingPublisher {
    void addObserver(BookingSubscriber observer);
    void removeObserver(BookingSubscriber observer);
    void notifyObservers(Long bookingId, String oldStatus, String newStatus);
}
