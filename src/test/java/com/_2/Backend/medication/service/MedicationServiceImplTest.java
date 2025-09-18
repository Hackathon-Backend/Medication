package com._2.Backend.medication.service;

import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MedicationServiceImplTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationServiceImpl medicationService;

    private MedicationRequest medicationRequest;
    private Medication medication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicationRequest = new MedicationRequest(
                "Paracetamol",
                "500mg",
                Frequency.ONCE_A_DAY,
                LocalTime.of(8, 0)
        );
        medication = new Medication(
                1L,
                "Paracetamol",
                "500mg",
                Frequency.ONCE_A_DAY,
                LocalTime.of(8, 0),
                true,
                false
        );
    }

    @Test
    void createMedication_ShouldReturnMedicationResponse() {
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        MedicationResponse response = medicationService.createMedication(medicationRequest);

        ArgumentCaptor<Medication> captor = ArgumentCaptor.forClass(Medication.class);
        verify(medicationRepository, times(1)).save(captor.capture());
        Medication saved = captor.getValue();

        assertThat(saved.getName()).isEqualTo(medicationRequest.getName());
        assertThat(saved.getDose()).isEqualTo(medicationRequest.getDose());
        assertThat(saved.getFrequency()).isEqualTo(medicationRequest.getFrequency());
        assertThat(saved.getTimeToTake()).isEqualTo(medicationRequest.getTimeToTake());
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.isTaken()).isFalse();

        assertThat(response.getName()).isEqualTo(medication.getName());
        assertThat(response.getDose()).isEqualTo(medication.getDose());
    }

    @Test
    void getAllMedications_ShouldReturnListOfMedicationResponse() {
        Medication medication2 = new Medication(
                2L,
                "Ibuprofeno",
                "200mg",
                Frequency.TWICE_A_DAY,
                LocalTime.of(12, 0),
                true,
                false
        );

        when(medicationRepository.findAll()).thenReturn(Arrays.asList(medication, medication2));

        List<MedicationResponse> responses = medicationService.getAllMedications();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("Paracetamol");
        assertThat(responses.get(1).getName()).isEqualTo("Ibuprofeno");
    }

    @Test
    void markAsTaken_ShouldSetTakenTrue() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        MedicationResponse response = medicationService.markAsTaken(1L);

        assertThat(response.isTaken()).isTrue();
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    void markAsTaken_ShouldThrowException_WhenNotFound() {
        when(medicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MedicationNotFoundException.class, () -> medicationService.markAsTaken(99L));
        verify(medicationRepository, never()).save(any());
    }

    @Test
    void deleteMedication_ShouldCallRepositoryDelete() {
        when(medicationRepository.existsById(1L)).thenReturn(true);

        medicationService.deleteMedication(1L);

        verify(medicationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedication_ShouldThrowException_WhenNotFound() {
        when(medicationRepository.existsById(99L)).thenReturn(false);

        assertThrows(MedicationNotFoundException.class, () -> medicationService.deleteMedication(99L));
        verify(medicationRepository, never()).deleteById(anyLong());
    }
}