package org.raflab.finansijeservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tax_rates",
        uniqueConstraints = @UniqueConstraint(columnNames = "taxName"))
public class TaxRates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taxName;
    private Double percentage;
}
