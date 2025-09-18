package com._2.Backend.medication;

import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MedicationControllerTest {

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private MedicationController medicationController;

    private MedicationRequest request;
    private MedicationResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new MedicationRequest();
        request.setName("Paracetamol");
        request.setDose("500mg");
        request.setFrequency(null); // En tests unitarios podemos usar null o un valor de Frequency si quieres
        request.setTimeToTake(LocalTime.of(8, 0));

        response = MedicationResponse.builder()
                .id(1L)
                .name("Paracetamol")
                .dose("500mg")
                .frequencyDisplay("Una vez al día")
                .timeToTake(LocalTime.of(8, 0))
                .active(true)
                .taken(false)
                .build();
    }

    @Test
    public void testCreateMedication() {
        when(medicationService.createMedication(request)).thenReturn(response);

        ResponseEntity<MedicationResponse> result = medicationController.createMedication(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(medicationService, times(1)).createMedication(request);
    }

    @Test
    public void testGetAllMedications() {
        List<MedicationResponse> medications = Arrays.asList(response);
        when(medicationService.getAllMedications()).thenReturn(medications);

        ResponseEntity<List<MedicationResponse>> result = medicationController.getAllMedications();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(medications, result.getBody());

        verify(medicationService, times(1)).getAllMedications();
    }

    @Test
    public void testMarkAsTaken() {
        when(medicationService.markAsTaken(1L)).thenReturn(response);

        ResponseEntity<MedicationResponse> result = medicationController.markAsTaken(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(medicationService, times(1)).markAsTaken(1L);
    }

    @Test
    public void testDeleteMedication() {
        doNothing().when(medicationService).deleteMedication(1L);

        ResponseEntity<Void> result = medicationController.deleteMedication(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(medicationService, times(1)).deleteMedication(1L);
    }
}