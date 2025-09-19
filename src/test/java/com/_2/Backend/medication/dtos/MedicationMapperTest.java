package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.Medication;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MedicationMapperTest {

    @Test
    void dtoToEntity_shouldMapAllFieldsCorrectly() {
        MedicationRequest request = new MedicationRequest();
        request.setName("Paracetamol");
        request.setDose("500mg");
        request.setFrequency(Frequency.TWICE_A_DAY);
        request.setTimeToTake(LocalTime.of(8, 0));
        request.setIntervalHours(8);
        request.setIntervalDays(1);

        Medication entity = MedicationMapper.dtoToEntity(request);

        assertNotNull(entity);
        assertEquals("Paracetamol", entity.getName());
        assertEquals("500mg", entity.getDose());
        assertEquals(Frequency.TWICE_A_DAY, entity.getFrequency());
        assertEquals(LocalTime.of(8, 0), entity.getTimeToTake());
        assertEquals(8, entity.getIntervalHours());
        assertEquals(1, entity.getIntervalDays());
    }

    @Test
    void entityToDto_shouldMapAllFieldsCorrectly() {
        Medication entity = new Medication();
        entity.setId(1L);
        entity.setName("Ibuprofeno");
        entity.setDose("400mg");
        entity.setFrequency(Frequency.THREE_TIMES_A_DAY);
        entity.setTimeToTake(LocalTime.of(9, 30));
        entity.setIntervalHours(0);
        entity.setIntervalDays(0);
        entity.setActive(true);

        MedicationResponse response = MedicationMapper.entityToDto(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Ibuprofeno", response.getName());
        assertEquals("400mg", response.getDose());
        assertEquals("Tres veces al día", response.getFrequencyDisplay());
        assertEquals(LocalTime.of(9, 30), response.getTimeToTake());
        assertEquals(0, response.getIntervalHours());
        assertEquals(0, response.getIntervalDays());
        assertTrue(response.isActive());
    }

    @Test
    void entityToDto_shouldReturnCustomFrequencyDisplay() {
        Medication entity = new Medication();
        entity.setId(2L);
        entity.setName("Vitamina C");
        entity.setDose("1 comprimido");
        entity.setFrequency(Frequency.CUSTOM);
        entity.setIntervalHours(12);
        entity.setIntervalDays(null); // Para que use horas
        entity.setTimeToTake(LocalTime.of(10, 0));
        entity.setActive(true);

        MedicationResponse response = MedicationMapper.entityToDto(entity);

        assertTrue(response.getFrequencyDisplay().contains("Cada 12 horas"));
    }

    @Test
    void entityToDto_shouldHandleCustomFrequencyWithoutIntervals() {
        Medication entity = new Medication();
        entity.setFrequency(Frequency.CUSTOM);
        entity.setIntervalHours(null);
        entity.setIntervalDays(null);

        MedicationResponse response = MedicationMapper.entityToDto(entity);

        assertEquals("Personalizar: Frecuencia no especificada", response.getFrequencyDisplay());
    }
}