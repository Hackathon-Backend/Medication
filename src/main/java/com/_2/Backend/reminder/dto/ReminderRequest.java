package com._2.Backend.reminder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ReminderRequest {
    @NotNull(message = "Medication ID is required")
    private Long medicationId;

    @NotNull(message = "Time is required")
    private LocalTime time;

    private Boolean active = true;
}

