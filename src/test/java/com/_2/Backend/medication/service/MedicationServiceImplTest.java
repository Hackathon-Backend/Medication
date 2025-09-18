package com._2.Backend.medication.service;

import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MedicationServiceImplTest {

    @Autowired
    private MedicationServiceImpl medicationService;

    @Autowired
    private MedicationRepository medicationRepository;

    @BeforeEach
    void setUp() {
        medicationRepository.deleteAll();
    }

    @Test
    void createMedication_ShouldReturnMedicationResponse() {
        MedicationRequest request = new MedicationRequest();
        request.setName("Paracetamol");
        request.setDose("500mg");
        request.setFrequency(Frequency.ONCE_A_DAY);
        request.setTimeToTake(LocalTime.of(8, 0));

        MedicationResponse response = medicationService.createMedication(request);

        assertNotNull(response.getId());
        assertEquals("Paracetamol", response.getName());
        assertEquals("500mg", response.getDose());

        List<Medication> meds = medicationRepository.findAll();
        assertEquals(1, meds.size());
        assertEquals("Paracetamol", meds.get(0).getName());
    }

    @Test
    void getAllMedications_ShouldReturnList() {
        Medication med1 = new Medication();
        med1.setName("Paracetamol");
        med1.setDose("500mg");
        med1.setFrequency(Frequency.ONCE_A_DAY);
        med1.setTimeToTake(LocalTime.of(8, 0));
        med1.setActive(true);

        Medication med2 = new Medication();
        med2.setName("Ibuprofeno");
        med2.setDose("200mg");
        med2.setFrequency(Frequency.TWICE_A_DAY);
        med2.setTimeToTake(LocalTime.of(12, 0));
        med2.setActive(true);

        medicationRepository.save(med1);
        medicationRepository.save(med2);

        List<MedicationResponse> responses = medicationService.getAllMedications();
        assertEquals(2, responses.size());
    }

    @Test
    void markAsTaken_ShouldThrowException_WhenNotFound() {
        assertThrows(MedicationNotFoundException.class, () -> medicationService.markAsTaken(99L));
    }

    @Test
    void deleteMedication_ShouldThrowException_WhenNotFound() {
        assertThrows(MedicationNotFoundException.class, () -> medicationService.deleteMedication(99L));
    }
}