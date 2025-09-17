package com._2.Backend.medication.dtos;

import com._2.Backend.medication.Medication;

public class MedicationMapper {
    public static Medication dtoToEntity (MedicationRequest dto) {
        Medication entity = new Medication();
        entity.setName(dto.getName());
        entity.setDosage(dto.getDosage());
        entity.setFrequency(dto.getFrequency());
        entity.setTimeToTake(dto.getTimeToTake());
        return entity;
    }

    public static MedicationResponse entityToDto(Medication entity) {
        MedicationResponse dto = new MedicationResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDosage(entity.getDosage());
        dto.setFrequency(entity.getFrequency());
        dto.setTimeToTake(entity.getTimeToTake());
        dto.setActive(entity.isActive());
        dto.setTaken(entity.isTaken());
        return dto;
    }
}
