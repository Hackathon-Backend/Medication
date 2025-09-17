package com._2.Backend.medication.service;

import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;

import java.util.List;

public interface MedicationService {
    MedicationResponse createMedication(MedicationRequest request);
    List<MedicationResponse> getAllMedications();
    MedicationResponse markAsTaken(Long id);
    void deleteMedication(Long id);
}
