package com._2.Backend.medication.config;

import com._2.Backend.config.GlobalExceptionHandler;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleMedicationNotFoundException_shouldReturnNotFoundStatusAndMessage() {
        MedicationNotFoundException ex = new MedicationNotFoundException(42L);

        ResponseEntity<String> response = handler.handleMedicationNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Medicamento no encontrado con id: 42", response.getBody());
    }
}