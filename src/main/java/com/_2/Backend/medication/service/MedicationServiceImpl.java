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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    @Override
    public MedicationResponse createMedication(MedicationRequest request) {
        // Validación de frecuencia CUSTOM
        if (request.getFrequency() == Frequency.CUSTOM) {
            if ((request.getIntervalHours() == null || request.getIntervalHours() <= 0)
                    && (request.getIntervalDays() == null || request.getIntervalDays() <= 0)) {
                throw new IllegalArgumentException(
                        "Para Personalizar, deberás especificar el intervalo de Horas o intervalo de Días"
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
    public Optional<MedicationResponse> getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .map(MedicationMapper::entityToDto);
    }

    @Override
    public MedicationResponse updateMedication(Long id, MedicationRequest request) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException(id));

        MedicationMapper.updateEntityFromDto(request, medication);

        if (request.getFrequency() == Frequency.CUSTOM) {
            if ((request.getIntervalHours() == null || request.getIntervalHours() <= 0)
                    && (request.getIntervalDays() == null || request.getIntervalDays() <= 0)) {
                throw new IllegalArgumentException(
                        "Para Personalizar, deberás especificar el intervalo de Horas o intervalo de Días"
                );
            }
        }

        medicationRepository.save(medication);
        return MedicationMapper.entityToDto(medication);
    }

    @Override
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new MedicationNotFoundException(id);
        }
        medicationRepository.deleteById(id);
    }
}