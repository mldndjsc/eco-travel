package org.raflab.finansijeservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "payment_methods",
        uniqueConstraints = @UniqueConstraint(columnNames = "methodName"))
public class PaymentMethods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String methodName;
    private Boolean isActive;
}
