package com._2.Backend.medication;

import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(@RequestBody MedicationRequest request) {
        MedicationResponse createdMedication = medicationService.createMedication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedication);
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getAllMedications() {
        List<MedicationResponse> medications = medicationService.getAllMedications();
        return ResponseEntity.ok(medications);
    }

    @PutMapping("/{id}/taken")
    public ResponseEntity<MedicationResponse> markAsTaken(@PathVariable Long id) {
        MedicationResponse updatedMedication = medicationService.markAsTaken(id);
        return ResponseEntity.ok(updatedMedication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
