package com._2.Backend.medication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class MedicationRequest {
    @NotBlank (message = "Medication name required")
    private String name;

    @NotBlank (message = "Medication dosage required")
    private String dosage;

    @NotBlank (message = "How frequently you will need to take this medication is required")
    private String frequency;

    @NotNull (message = "The time when the medication should be taken is required")
    private LocalTime timeToTake;
}
