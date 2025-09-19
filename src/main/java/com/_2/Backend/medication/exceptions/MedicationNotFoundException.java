package com._2.Backend.medication.exceptions;

public class MedicationNotFoundException extends RuntimeException {

    public MedicationNotFoundException(Long id) {
        super("Medicamento no encontrado con id: " + id);
    }
}