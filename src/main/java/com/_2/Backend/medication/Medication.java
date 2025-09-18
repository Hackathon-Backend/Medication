package com._2.Backend.medication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String dose;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @NotNull
    private LocalTime timeToTake;

    private Integer intervalHours;
    private Integer intervalDays;

    private boolean active = true;
}