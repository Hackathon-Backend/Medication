package com._2.Backend.reminder.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class ReminderResponse {
    private Long id;
    private LocalTime time;
    private Boolean active;
    private Boolean taken;
    private String medicationName;

}
