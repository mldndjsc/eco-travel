package org.raflab.rezervacijeservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AddOnItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Bookings booking;
    private String serviceName;
    private Double price;
}
