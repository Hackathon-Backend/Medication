package com._2.Backend.medication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MedicationRepositoryTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @BeforeEach
    void setUp() {
        medicationRepository.deleteAll();
    }

    @Test
    void saveAndFindMedication() {
        Medication med = new Medication();
        med.setName("Paracetamol");
        med.setDose("500mg");
        med.setFrequency(Frequency.ONCE_A_DAY);
        med.setTimeToTake(LocalTime.of(8, 0));
        med.setActive(true);

        Medication saved = medicationRepository.save(med);

        Optional<Medication> found = medicationRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Paracetamol", found.get().getName());
    }

    @Test
    void findAllMedications() {
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

        List<Medication> all = medicationRepository.findAll();
        assertEquals(2, all.size());
    }
}