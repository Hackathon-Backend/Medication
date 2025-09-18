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
import org.springframework.http.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedicationControllerTest {

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
            request.setFrequency(null);
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

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertEquals(response, result.getBody());
            verify(medicationService, times(1)).createMedication(request);
        }

        @Test
        void testGetAllMedications_Unit() {
            List<MedicationResponse> medications = Arrays.asList(response);
            when(medicationService.getAllMedications()).thenReturn(medications);

            ResponseEntity<List<MedicationResponse>> result = medicationController.getAllMedications();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(medications, result.getBody());
            verify(medicationService, times(1)).getAllMedications();
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
            req.setFrequency(null);
            req.setTimeToTake(LocalTime.of(9, 0));

            ResponseEntity<MedicationResponse> responseEntity = restTemplate.postForEntity(
                    "/medications",
                    req,
                    MedicationResponse.class
            );

            MedicationResponse body = responseEntity.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals("Ibuprofeno", body.getName());
        }

        @Test
        void testGetAllMedications_Integration() {
            ResponseEntity<MedicationResponse[]> responseEntity = restTemplate.getForEntity(
                    "/medications",
                    MedicationResponse[].class
            );

            MedicationResponse[] body = responseEntity.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
    }
}