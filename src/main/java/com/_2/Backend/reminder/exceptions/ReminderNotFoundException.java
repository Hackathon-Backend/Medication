package com._2.Backend.reminder.exceptions;

public class ReminderNotFoundException extends RuntimeException {
    public ReminderNotFoundException(Long id) {
        super("Recordatorio no encontrado con id: " + id);
    }
}