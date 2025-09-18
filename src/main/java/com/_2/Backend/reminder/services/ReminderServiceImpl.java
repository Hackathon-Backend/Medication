package com._2.Backend.reminder.services;

import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import com._2.Backend.reminder.Reminder;
import com._2.Backend.reminder.ReminderRepository;
import com._2.Backend.reminder.dtos.ReminderMapper;
import com._2.Backend.reminder.dtos.ReminderRequest;
import com._2.Backend.reminder.dtos.ReminderResponse;
import com._2.Backend.reminder.exceptions.InvalidReminderTimeException;
import com._2.Backend.reminder.exceptions.ReminderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final MedicationRepository medicationRepository;

    @Override
    public List<ReminderResponse> getAllReminders() {
        return reminderRepository.findAll()
                .stream()
                .map(ReminderMapper::entityToDto)
                .toList();
    }

    @Override
    public List<ReminderResponse> getActiveReminders() {
        return reminderRepository.findByActiveTrue()
                .stream()
                .map(ReminderMapper::entityToDto)
                .toList();
    }

    @Override
    public ReminderResponse createReminder(ReminderRequest request) {
        if (request.getTime() == null || request.getTime().isBefore(LocalTime.now())) {
            throw new InvalidReminderTimeException("La hora no puede ser nula ni estar en el pasado");
        }

        Medication medication = medicationRepository.findById(request.getMedicationId())
                .orElseThrow(() -> new MedicationNotFoundException(request.getMedicationId()));

        Reminder reminder = Reminder.builder()
                .time(request.getTime())
                .active(request.getActive())
                .taken(false)
                .medication(medication)
                .build();

        Reminder saved = reminderRepository.save(reminder);
        return ReminderMapper.entityToDto(saved);
    }

    @Override
    public ReminderResponse markAsTaken(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ReminderNotFoundException(id));

        reminder.setTaken(true);
        reminderRepository.save(reminder);

        return ReminderMapper.entityToDto(reminder);
    }
}