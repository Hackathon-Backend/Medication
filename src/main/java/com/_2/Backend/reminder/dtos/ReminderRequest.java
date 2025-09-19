package com._2.Backend.reminder.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class ReminderRequest {
    @NotNull(message = "El ID del medicamento es obligatorio")
    private Long medicationId;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime time;

    @Schema(description = "Indica si el recordatorio está activo", defaultValue = "true")
    private Boolean active = true;
}

