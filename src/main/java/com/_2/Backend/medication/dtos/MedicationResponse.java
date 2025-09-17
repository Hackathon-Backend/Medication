package com._2.Backend.medication.dtos;

import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class MedicationResponse {
    private Long id;
    private String name;
    private String dosage;
    private String frequency;
    private LocalTime timeToTake;
    private boolean active;
    private boolean taken;
}
