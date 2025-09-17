package com._2.Backend.medication.dtos;

import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MedicationResponse {
    private Long id;
    private String name;
    private String dose;
    private String frequencyDisplay;
    private LocalTime timeToTake;
    private boolean active;
    private boolean taken;
}