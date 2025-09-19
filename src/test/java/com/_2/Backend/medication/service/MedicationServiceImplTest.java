package com._2.Backend.medication.service;

import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicationServiceImplTest {

    @Nested
    class UnitTests {

        @Mock
        private MedicationRepository mockRepository;

        @InjectMocks
        private MedicationServiceImpl medicationService;

        private Medication medication;
        private MedicationRequest request;

        @BeforeEach
        void setUp() {
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

            assertNotNull(response);
            assertEquals("Paracetamol", response.getName());
        }

        @Test
        void createMedication_CustomFrequencyWithoutInterval_ShouldThrowException() {
            request.setFrequency(Frequency.CUSTOM);
            request.setIntervalHours(null);
            request.setIntervalDays(null);

            assertThrows(IllegalArgumentException.class, () -> medicationService.createMedication(request));
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
        void getMedicationById_Found_ShouldReturnResponse() {
            when(mockRepository.findById(1L)).thenReturn(Optional.of(medication));
            Optional<MedicationResponse> result = medicationService.getMedicationById(1L);
            assertTrue(result.isPresent());
            assertEquals("Paracetamol", result.get().getName());
        }

        @Test
        void getMedicationById_NotFound_ShouldReturnEmpty() {
            when(mockRepository.findById(99L)).thenReturn(Optional.empty());
            Optional<MedicationResponse> result = medicationService.getMedicationById(99L);
            assertTrue(result.isEmpty());
        }

        @Test
        void updateMedication_ShouldUpdateFields() {
            when(mockRepository.findById(1L)).thenReturn(Optional.of(medication));
            when(mockRepository.save(any(Medication.class))).thenReturn(medication);

            MedicationRequest updateRequest = new MedicationRequest();
            updateRequest.setName("Updated");
            updateRequest.setDose("300mg");
            updateRequest.setFrequency(Frequency.TWICE_A_DAY);
            updateRequest.setTimeToTake(LocalTime.of(14,0));

            MedicationResponse response = medicationService.updateMedication(1L, updateRequest);
            assertEquals("Updated", response.getName());
            assertEquals("300mg", response.getDose());
        }

        @Test
        void updateMedication_NotFound_ShouldThrowException() {
            when(mockRepository.findById(99L)).thenReturn(Optional.empty());
            MedicationRequest updateRequest = new MedicationRequest();
            assertThrows(MedicationNotFoundException.class, () -> medicationService.updateMedication(99L, updateRequest));
        }

        @Test
        void updateMedication_CustomFrequencyWithoutInterval_ShouldThrowException() {
            when(mockRepository.findById(1L)).thenReturn(Optional.of(medication));
            MedicationRequest custom = new MedicationRequest();
            custom.setFrequency(Frequency.CUSTOM);
            assertThrows(IllegalArgumentException.class, () -> medicationService.updateMedication(1L, custom));
        }

        @Test
        void deleteMedication_ShouldCallRepository() {
            when(mockRepository.existsById(1L)).thenReturn(true);
            medicationService.deleteMedication(1L);
            verify(mockRepository).deleteById(1L);
        }

        @Test
        void deleteMedication_NotFound_ShouldThrowException() {
            when(mockRepository.existsById(99L)).thenReturn(false);
            assertThrows(MedicationNotFoundException.class, () -> medicationService.deleteMedication(99L));
            verify(mockRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @SpringBootTest
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
            assertEquals("250mg", response.getDose());
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

        @Test
        void createMedication_CustomFrequency_Integration() {
            MedicationRequest req = new MedicationRequest();
            req.setName("MedCustom");
            req.setDose("100mg");
            req.setFrequency(Frequency.CUSTOM);
            req.setIntervalHours(6);
            req.setTimeToTake(LocalTime.of(9,0));

            MedicationResponse response = medicationServiceImpl.createMedication(req);
            assertNotNull(response.getId());
            assertEquals("MedCustom", response.getName());
        }

        @Test
        void updateMedication_Integration() {
            Medication med = new Medication();
            med.setName("Ibuprofeno");
            med.setDose("200mg");
            med.setFrequency(Frequency.ONCE_A_DAY);
            med.setTimeToTake(LocalTime.of(8,0));
            med.setActive(true);

            medicationRepository.save(med);

            MedicationRequest updateReq = new MedicationRequest();
            updateReq.setName("Ibuprofeno Updated");
            updateReq.setDose("400mg");
            updateReq.setFrequency(Frequency.TWICE_A_DAY);
            updateReq.setTimeToTake(LocalTime.of(14,0));

            MedicationResponse response = medicationServiceImpl.updateMedication(med.getId(), updateReq);
            assertEquals("Ibuprofeno Updated", response.getName());
            assertEquals("400mg", response.getDose());
        }

        @Test
        void deleteMedication_Integration() {
            Medication med = new Medication();
            med.setName("Paracetamol");
            med.setDose("500mg");
            med.setFrequency(Frequency.ONCE_A_DAY);
            med.setTimeToTake(LocalTime.of(8,0));
            med.setActive(true);

            medicationRepository.save(med);

            medicationServiceImpl.deleteMedication(med.getId());
            assertEquals(0, medicationRepository.count());
        }
    }
}