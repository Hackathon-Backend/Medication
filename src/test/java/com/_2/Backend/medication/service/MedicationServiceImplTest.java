package com._2.Backend.medication.service;

import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MedicationServiceImplTest {

    @Nested
    class UnitTests {

        @Mock
        private MedicationRepository mockRepository;

        @InjectMocks
        private MedicationServiceImpl medicationService;

        private MedicationRequest request;
        private Medication medication;

        @BeforeEach
        void setUpUnit() {
            MockitoAnnotations.openMocks(this);

            request = new MedicationRequest();
            request.setName("Paracetamol");
            request.setDose("500mg");
            request.setFrequency(Frequency.ONCE_A_DAY);
            request.setTimeToTake(LocalTime.of(8, 0));

            medication = new Medication();
            medication.setId(1L);
            medication.setName("Paracetamol");
            medication.setDose("500mg");
            medication.setFrequency(Frequency.ONCE_A_DAY);
            medication.setTimeToTake(LocalTime.of(8, 0));
            medication.setActive(true);
        }

        @Test
        void createMedication_ShouldReturnMedicationResponse() {
            when(mockRepository.save(any(Medication.class))).thenReturn(medication);

            MedicationResponse response = medicationService.createMedication(request);

            assertThat(response.getName()).isEqualTo("Paracetamol");
            assertThat(response.getDose()).isEqualTo("500mg");
        }

        @Test
        void getAllMedications_ShouldReturnList() {
            Medication med2 = new Medication();
            med2.setId(2L);
            med2.setName("Ibuprofeno");
            med2.setDose("200mg");
            med2.setFrequency(Frequency.TWICE_A_DAY);
            med2.setTimeToTake(LocalTime.of(12, 0));
            med2.setActive(true);

            when(mockRepository.findAll()).thenReturn(Arrays.asList(medication, med2));

            List<MedicationResponse> responses = medicationService.getAllMedications();
            assertEquals(2, responses.size());
        }

        @Test
        void deleteMedication_ShouldThrowException_WhenNotFound() {
            when(mockRepository.existsById(99L)).thenReturn(false);
            assertThrows(MedicationNotFoundException.class, () -> medicationService.deleteMedication(99L));
            verify(mockRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    class IntegrationTests {

        @Autowired
        private MedicationServiceImpl medicationServiceImpl;

        @Autowired
        private MedicationRepository medicationRepository;

        @BeforeEach
        void setUpIntegration() {
            medicationRepository.deleteAll();
        }

        @Test
        void createMedication_Integration() {
            MedicationRequest req = new MedicationRequest();
            req.setName("Amoxicilina");
            req.setDose("250mg");
            req.setFrequency(Frequency.ONCE_A_DAY);
            req.setTimeToTake(LocalTime.of(10, 0));

            MedicationResponse response = medicationServiceImpl.createMedication(req);
            assertNotNull(response.getId());
            assertEquals("Amoxicilina", response.getName());
        }

        @Test
        void getAllMedications_Integration() {
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

            List<MedicationResponse> responses = medicationServiceImpl.getAllMedications();
            assertEquals(2, responses.size());
        }
    }
}