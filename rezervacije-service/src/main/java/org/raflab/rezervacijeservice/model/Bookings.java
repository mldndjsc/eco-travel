package org.raflab.rezervacijeservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer unitId;
    @ManyToOne
    private Guests guest;
    @ManyToOne
    private CancellationPolicies policy;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private String status;
}
