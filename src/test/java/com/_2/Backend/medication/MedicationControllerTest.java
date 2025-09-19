package com._2.Backend.medication;

import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedicationControllerTest {

    private final String BASE_URL = "/medications";

    @Nested
    class UnitTests {

        @Mock
        private MedicationService medicationService;

        @InjectMocks
        private MedicationController medicationController;

        private MedicationRequest request;
        private MedicationResponse response;

        @BeforeEach
        void setUpUnit() {
            MockitoAnnotations.openMocks(this);

            request = new MedicationRequest();
            request.setName("Paracetamol");
            request.setDose("500mg");
            request.setFrequency(Frequency.ONCE_A_DAY);
            request.setTimeToTake(LocalTime.of(8, 0));

            response = MedicationResponse.builder()
                    .id(1L)
                    .name("Paracetamol")
                    .dose("500mg")
                    .frequencyDisplay("Una vez al día")
                    .timeToTake(LocalTime.of(8, 0))
                    .active(true)
                    .build();
        }

        @Test
        void testCreateMedication_Unit() {
            when(medicationService.createMedication(request)).thenReturn(response);

            ResponseEntity<MedicationResponse> result = medicationController.createMedication(request);

            assertNotNull(result);
            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertEquals(response, result.getBody());
            verify(medicationService, times(1)).createMedication(request);
        }

        @Test
        void testGetAllMedications_Unit() {
            List<MedicationResponse> medications = Collections.singletonList(response);
            when(medicationService.getAllMedications()).thenReturn(medications);

            ResponseEntity<List<MedicationResponse>> result = medicationController.getAllMedications();

            assertNotNull(result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(medications, result.getBody());
            verify(medicationService, times(1)).getAllMedications();
        }

        @Test
        void testUpdateMedication_Unit() {
            Long medicationId = 1L;
            MedicationRequest updateRequest = new MedicationRequest();
            updateRequest.setName("Paracetamol actualizado");
            updateRequest.setDose("1000mg");
            updateRequest.setFrequency(Frequency.TWICE_A_DAY);
            updateRequest.setTimeToTake(LocalTime.of(10, 0));

            MedicationResponse updatedResponse = MedicationResponse.builder()
                    .id(medicationId)
                    .name("Paracetamol actualizado")
                    .dose("1000mg")
                    .frequencyDisplay("Dos veces al día")
                    .timeToTake(LocalTime.of(10, 0))
                    .active(true)
                    .build();

            when(medicationService.updateMedication(eq(medicationId), any(MedicationRequest.class)))
                    .thenReturn(updatedResponse);

            ResponseEntity<MedicationResponse> result = medicationController.updateMedication(medicationId, updateRequest);

            assertNotNull(result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(updatedResponse, result.getBody());
            verify(medicationService, times(1)).updateMedication(medicationId, updateRequest);
        }

        @Test
        void testDeleteMedication_Unit() {
            Long medicationId = 1L;
            doNothing().when(medicationService).deleteMedication(medicationId);

            ResponseEntity<Void> result = medicationController.deleteMedication(medicationId);

            assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
            verify(medicationService, times(1)).deleteMedication(medicationId);
        }
    }

    @Nested
    class IntegrationTests {

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private MedicationRepository medicationRepository;

        @BeforeEach
        void setUpIntegration() {
            medicationRepository.deleteAll();
        }

        @Test
        void testCreateMedication_Integration() {
            MedicationRequest req = new MedicationRequest();
            req.setName("Ibuprofeno");
            req.setDose("200mg");
            req.setFrequency(Frequency.ONCE_A_DAY);
            req.setTimeToTake(LocalTime.of(9, 0));

            ResponseEntity<MedicationResponse> responseEntity = restTemplate.postForEntity(
                    BASE_URL,
                    req,
                    MedicationResponse.class
            );

            MedicationResponse body = responseEntity.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals("Ibuprofeno", body.getName());
            assertEquals("200mg", body.getDose());
            assertEquals("Una vez al día", body.getFrequencyDisplay());
            assertEquals(1, medicationRepository.count());
        }

        @Test
        void testGetAllMedications_Integration() {
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

            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

            ResponseEntity<List<MedicationResponse>> responseEntity = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<MedicationResponse>>() {}
            );

            List<MedicationResponse> body = responseEntity.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(2, body.size());
            assertEquals(2, medicationRepository.count());
        }

        @Test
        void testUpdateMedication_Integration() {
            Medication originalMed = new Medication();
            originalMed.setName("Vitamina C");
            originalMed.setDose("1000mg");
            originalMed.setFrequency(Frequency.ONCE_A_DAY);
            originalMed.setTimeToTake(LocalTime.of(10, 0));
            originalMed.setActive(true);
            Medication savedMed = medicationRepository.save(originalMed);

            MedicationRequest updateRequest = new MedicationRequest();
            updateRequest.setName("Vitamina C actualizada");
            updateRequest.setDose("500mg");
            updateRequest.setFrequency(Frequency.TWICE_A_DAY);
            updateRequest.setTimeToTake(LocalTime.of(11, 0));

            HttpEntity<MedicationRequest> requestEntity = new HttpEntity<>(updateRequest);

            ResponseEntity<MedicationResponse> responseEntity = restTemplate.exchange(
                    BASE_URL + "/" + savedMed.getId(),
                    HttpMethod.PUT,
                    requestEntity,
                    MedicationResponse.class
            );

            MedicationResponse body = responseEntity.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("Vitamina C actualizada", body.getName());
            assertEquals("500mg", body.getDose());
            assertEquals("Dos veces al día", body.getFrequencyDisplay());
        }

        @Test
        void testDeleteMedication_Integration() {
            Medication medToDelete = new Medication();
            medToDelete.setName("Aspirina");
            medToDelete.setDose("100mg");
            medToDelete.setFrequency(Frequency.ONCE_A_DAY);
            medToDelete.setTimeToTake(LocalTime.of(15, 0));
            medToDelete.setActive(true);
            Medication savedMed = medicationRepository.save(medToDelete);

            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

            restTemplate.exchange(
                    BASE_URL + "/" + savedMed.getId(),
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class
            );

            assertFalse(medicationRepository.findById(savedMed.getId()).isPresent());
        }
    }
}