package org.raflab.rezervacijeservice.service;

import org.raflab.rezervacijeservice.observer.BookingSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusLogSubscriber implements BookingSubscriber {

    @Autowired
    private BookingStatusLogsService logService;


    @Override
    public void update(Long bookingId, String oldStatus, String newStatus) {
        System.out.println("SUBSCRIBER REAGUJE: Rezervacija " + bookingId +
                " je promenila status iz " + oldStatus + " u " + newStatus);
        logService.logStatusChange(bookingId, oldStatus, newStatus);
    }
}
