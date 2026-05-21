package org.raflab.finansijeservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceNumber;
    private Double subtotal;
    private Double total_amount;
    private String status;
    private Long bookingId;
    @ManyToOne
    private TaxRates taxRates;
}
