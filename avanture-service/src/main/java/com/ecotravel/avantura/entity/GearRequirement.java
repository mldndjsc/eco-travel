package com.ecotravel.avantura.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gear_requirement")
@Getter
@Setter
public class GearRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adventure_id", nullable = false)
    private Adventure adventure;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = true;
}
