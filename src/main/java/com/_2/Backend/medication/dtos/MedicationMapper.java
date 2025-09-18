package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.Medication;

public class MedicationMapper {

    public static Medication dtoToEntity (MedicationRequest dto) {
        Medication entity = new Medication();
        entity.setName(dto.getName());
        entity.setDose(dto.getDose());
        entity.setFrequency(dto.getFrequency());
        entity.setTimeToTake(dto.getTimeToTake());
        entity.setIntervalHours(dto.getIntervalHours());
        entity.setIntervalDays(dto.getIntervalDays());
        return entity;
    }

    public static void updateEntityFromDto(MedicationRequest dto, Medication entity) {
        entity.setName(dto.getName());
        entity.setDose(dto.getDose());
        entity.setFrequency(dto.getFrequency());
        entity.setTimeToTake(dto.getTimeToTake());
        entity.setIntervalHours(dto.getIntervalHours());
        entity.setIntervalDays(dto.getIntervalDays());
    }

    public static MedicationResponse entityToDto(Medication entity) {
        return MedicationResponse.builder()
        .id(entity.getId())
        .name(entity.getName())
        .dose(entity.getDose())
        .frequencyDisplay(entity.getFrequency().getDisplayInSpanish())
        .timeToTake(entity.getTimeToTake())
        .intervalHours(entity.getIntervalHours())
        .intervalDays(entity.getIntervalDays())
        .active(entity.isActive())
        .build();
    }

    private static String getFrequencyDisplay(Medication entity) {
        if (entity.getFrequency() == Frequency.CUSTOM) {
            StringBuilder display = new StringBuilder("Personalizar: ");
            if (entity.getIntervalDays() != null && entity.getIntervalDays() > 0) {
                display.append(entity.getIntervalDays() == 1 ? "Todos los días" : "Cada" + entity.getIntervalDays() + " días");
            } else if (entity.getIntervalHours() != null && entity.getIntervalHours() > 0) {
                display.append("Cada ").append(entity.getIntervalHours()).append("horas");
            }
            return display.toString();
        }
        return entity.getFrequency().getDisplayInSpanish();
    }
}