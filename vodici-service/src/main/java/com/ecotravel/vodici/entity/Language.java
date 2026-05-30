package com.ecotravel.vodici.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "language")
@Getter
@Setter
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language_name", nullable = false, unique = true)
    private String languageName;

    @Column(name = "language_code", nullable = false, unique = true, length = 10)
    private String languageCode;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuideLanguage> guideLanguages = new ArrayList<>();
}
