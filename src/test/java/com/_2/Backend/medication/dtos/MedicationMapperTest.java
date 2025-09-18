package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.Medication;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MedicationMapperTest {

    @Test
    void dtoToEntity_shouldMapAllFieldsCorrectly() {
        MedicationRequest request = new MedicationRequest(
                "Paracetamol",
                "500mg",
                Frequency.TWICE_A_DAY,
                LocalTime.of(8, 0)
        );

        Medication entity = MedicationMapper.dtoToEntity(request);

        assertNotNull(entity);
        assertEquals("Paracetamol", entity.getName());
        assertEquals("500mg", entity.getDose());
        assertEquals(Frequency.TWICE_A_DAY, entity.getFrequency());
        assertEquals(LocalTime.of(8, 0), entity.getTimeToTake());
        assertFalse(entity.isTaken());
        assertTrue(entity.isActive());
    }

    @Test
    void entityToDto_shouldMapAllFieldsCorrectly() {
        Medication entity = new Medication(
                1L,
                "Ibuprofeno",
                "400mg",
                Frequency.THREE_TIMES_A_DAY,
                LocalTime.of(9, 30),
                true,
                false
        );

        MedicationResponse response = MedicationMapper.entityToDto(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Ibuprofeno", response.getName());
        assertEquals("400mg", response.getDose());
        assertEquals("Tres veces al día", response.getFrequencyDisplay());
        assertEquals(LocalTime.of(9, 30), response.getTimeToTake());
        assertTrue(response.isActive());
        assertFalse(response.isTaken());
    }
}