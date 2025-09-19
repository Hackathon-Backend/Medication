package com._2.Backend.reminder.exceptions;

public class InvalidReminderTimeException extends RuntimeException {
    public InvalidReminderTimeException(String message) {
        super("Hora inválida para el recordatorio: " + message);
    }
}
