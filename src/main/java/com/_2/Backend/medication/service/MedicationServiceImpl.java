package com._2.Backend.medication.service;

import com._2.Backend.medication.Frequency;
import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.dtos.MedicationMapper;
import com._2.Backend.medication.dtos.MedicationRequest;
import com._2.Backend.medication.dtos.MedicationResponse;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl  implements MedicationService{
    private final MedicationRepository medicationRepository;

    @Override
    public MedicationResponse createMedication(MedicationRequest request) {
        if (request.getFrequency() == Frequency.CUSTOM) {
            if ((request.getIntervalHours() == null || request.getIntervalHours() <= 0) && (request.getIntervalDays() == null || request.getIntervalDays() <= 0)) {
                throw new IllegalArgumentException(
                        "Para Personalizar, deberás especificar el intérvalo de Horas o intérvalo de Días"
                );
            }
        }

        Medication newMedication = MedicationMapper.dtoToEntity(request);
        newMedication.setActive(true);

        Medication savedMedication = medicationRepository.save(newMedication);
        return MedicationMapper.entityToDto(savedMedication);
    }

    @Override
    public List<MedicationResponse> getAllMedications() {
        return medicationRepository.findAll()
                .stream()
                .map(MedicationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MedicationResponse markAsTaken(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException(id));

        Medication updatedMedication = medicationRepository.save(medication);

        return MedicationMapper.entityToDto(updatedMedication);
    }

    @Override
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new MedicationNotFoundException(id);
        }
        medicationRepository.deleteById(id);
    }
}