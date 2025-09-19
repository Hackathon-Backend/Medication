package com._2.Backend.medication;

import com._2.Backend.medication.dtos.MedicationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedicationControllerTest {

    private static final String BASE_URL = "/api/medications";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MedicationRepository medicationRepository;

    @BeforeEach
    void setUp() {
        // Limpiamos la base de datos antes de cada test
        medicationRepository.deleteAll();
    }

    @Test
    void testGetAllMedications_Integration() {
        // Creamos medicamentos de prueba
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

        // Hacemos la petición GET
        ResponseEntity<List<MedicationResponse>> responseEntity = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<MedicationResponse>>() {}
        );

        // Depuración: imprimimos el JSON recibido
        System.out.println("Response JSON: " + responseEntity.getBody());

        // Validamos la respuesta
        List<MedicationResponse> body = responseEntity.getBody();
        assertNotNull(body, "La lista de medicamentos no debe ser null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "El status debe ser 200 OK");
        assertEquals(2, body.size(), "Debe devolver 2 medicamentos");
        assertEquals(2, medicationRepository.count(), "Debe haber 2 medicamentos en la base de datos");
    }
}