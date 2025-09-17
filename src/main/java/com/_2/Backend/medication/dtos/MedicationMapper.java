package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Medication;

public class MedicationMapper {

    public static Medication dtoToEntity (MedicationRequest dto) {
        Medication entity = new Medication();
        entity.setName(dto.getName());
        entity.setDose(dto.getDose());
        entity.setFrequency(dto.getFrequency());
        entity.setTimeToTake(dto.getTimeToTake());
        return entity;
    }

    public static MedicationResponse entityToDto(Medication entity) {
        return MedicationResponse.builder()
        .id(entity.getId())
        .name(entity.getName())
        .dose(entity.getDose())
        .frequencyDisplay(entity.getFrequency().getDisplayInSpanish())
        .timeToTake(entity.getTimeToTake())
        .active(entity.isActive())
        .taken(entity.isTaken())
        .build();
    }
}