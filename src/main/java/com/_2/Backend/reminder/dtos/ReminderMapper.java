package com._2.Backend.reminder.dtos;

import com._2.Backend.reminder.Reminder;

public class ReminderMapper {

    public static ReminderResponse entityToDto(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .time(reminder.getTime())
                .active(reminder.getActive())
                .taken(reminder.getTaken())
                .medicationName(reminder.getMedication().getName())
                .build();
    }
}
