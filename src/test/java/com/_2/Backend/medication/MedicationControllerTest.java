package com._2.Backend.medication;

import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
        assertNotNull(body, "El body de la respuesta no debería ser null");
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
        assertNotNull(body, "El body de la respuesta no debería ser null");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(body.length >= 0);
    }
}