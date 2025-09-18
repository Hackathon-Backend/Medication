package com._2.Backend.medication.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicationNotFoundExceptionTest {

    @Test
    void constructor_shouldSetCorrectMessage() {
        MedicationNotFoundException ex = new MedicationNotFoundException(99L);

        assertEquals("Medicamento no encontrado con id: 99", ex.getMessage());
    }
}