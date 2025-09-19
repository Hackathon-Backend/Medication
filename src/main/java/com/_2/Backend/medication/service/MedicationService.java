package com._2.Backend.medication.service;

import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;

import java.util.List;
import java.util.Optional;

public interface MedicationService {
    MedicationResponse createMedication(MedicationRequest request);
    List<MedicationResponse> getAllMedications();
    MedicationResponse updateMedication(Long id, MedicationRequest request);
    void deleteMedication(Long id);
    Optional<MedicationResponse> getMedicationById(Long id);
}