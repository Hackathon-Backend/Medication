package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class MedicationRequest {
    @NotBlank (message = "Nombre del medicamento requerido")
    private String name;

    @NotBlank (message = "Dosis del medicamento requerida")
    private String dose;

    @NotBlank (message = "La frecuencia de la toma del medicamento requerida")
    private Frequency frequency;

    @NotNull (message = "Hora a tomar el medicamento requerida")
    private LocalTime timeToTake;
}
