package com._2.Backend.security;

import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import com._2.Backend.reminder.exceptions.InvalidReminderTimeException;
import com._2.Backend.reminder.exceptions.ReminderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicationNotFoundException.class)
    public ResponseEntity<String> handleMedicationNotFoundException(MedicationNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ReminderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReminderNotFound(ReminderNotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "NO_ENCONTRADO");
        error.put("mensaje", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidReminderTimeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidReminderTime(InvalidReminderTimeException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "DATOS_INVÁLIDOS");
        error.put("mensaje", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "ERROR_INTERNO");
        error.put("mensaje", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
