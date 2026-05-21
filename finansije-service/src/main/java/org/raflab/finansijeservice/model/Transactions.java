package org.raflab.finansijeservice.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private LocalDateTime date;
    @ManyToOne
    private PaymentMethods paymentMethod;
    @ManyToOne
    private Invoices invoice;


}
