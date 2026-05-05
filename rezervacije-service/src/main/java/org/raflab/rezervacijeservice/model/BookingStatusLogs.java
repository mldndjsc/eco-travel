package org.raflab.rezervacijeservice.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
public class BookingStatusLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Bookings booking;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;
}
