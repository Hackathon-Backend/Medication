package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Frequency;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class MedicationRequest {
    @NotNull (message = "Nombre del medicamento requerido")
    private String name;

    @NotNull (message = "Dosis del medicamento requerida")
    private String dose;

    @NotNull (message = "La frecuencia de la toma del medicamento requerida")
    private Frequency frequency;

    @NotNull (message = "Hora a tomar el medicamento requerida")
    private LocalTime timeToTake;

    private Integer intervalHours;
    private Integer intervalDays;
}
