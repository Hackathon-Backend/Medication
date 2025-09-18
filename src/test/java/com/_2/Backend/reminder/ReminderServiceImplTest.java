package com._2.Backend.reminder;

import com._2.Backend.medication.Medication;
import com._2.Backend.medication.MedicationRepository;
import com._2.Backend.medication.exceptions.MedicationNotFoundException;
import com._2.Backend.reminder.dtos.ReminderRequest;
import com._2.Backend.reminder.dtos.ReminderResponse;
import com._2.Backend.reminder.exceptions.InvalidReminderTimeException;
import com._2.Backend.reminder.exceptions.ReminderNotFoundException;
import com._2.Backend.reminder.services.ReminderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReminderServiceImplTest {

    private ReminderRepository reminderRepository;
    private MedicationRepository medicationRepository;
    private ReminderServiceImpl reminderService;

    @BeforeEach
    void setUp() {
        reminderRepository = mock(ReminderRepository.class);
        medicationRepository = mock(MedicationRepository.class);
        reminderService = new ReminderServiceImpl(reminderRepository, medicationRepository);
    }

    @Test
    void createReminder_success() {
        LocalTime futureTime = LocalTime.now().plusMinutes(5);
        Medication medication = new Medication();
        medication.setId(10L);
        medication.setName("Ibuprofeno");

        when(medicationRepository.findById(10L)).thenReturn(Optional.of(medication));
        when(reminderRepository.save(any(Reminder.class)))
                .thenAnswer(invocation -> {
                    Reminder reminderToSave = invocation.getArgument(0);
                    reminderToSave.setId(1L);
                    return reminderToSave;
                });

        ReminderRequest reminderRequest = ReminderRequest.builder()
                .time(futureTime)
                .active(true)
                .medicationId(10L)
                .build();

        ReminderResponse createdReminder = reminderService.createReminder(reminderRequest);

        assertThat(createdReminder.getId()).isEqualTo(1L);
        assertThat(createdReminder.getTime()).isEqualTo(futureTime);
        assertThat(createdReminder.getActive()).isTrue();
        assertThat(createdReminder.getTaken()).isFalse();

        ArgumentCaptor<Reminder> reminderCaptor = ArgumentCaptor.forClass(Reminder.class);
        verify(reminderRepository).save(reminderCaptor.capture());
        assertThat(reminderCaptor.getValue().getMedication().getId()).isEqualTo(10L);
    }

    @Test
    void createReminder_pastTime_throws() {
        ReminderRequest reminderRequest = ReminderRequest.builder()
                .time(LocalTime.now().minusMinutes(1))
                .active(true)
                .medicationId(1L)
                .build();

        assertThatThrownBy(() -> reminderService.createReminder(reminderRequest))
                .isInstanceOf(InvalidReminderTimeException.class);

        verifyNoInteractions(medicationRepository);
        verifyNoInteractions(reminderRepository);
    }

    @Test
    void createReminder_medicationNotFound_throws() {
        LocalTime futureTime = LocalTime.now().plusMinutes(5);
        when(medicationRepository.findById(999L)).thenReturn(Optional.empty());

        ReminderRequest reminderRequest = ReminderRequest.builder()
                .time(futureTime)
                .active(true)
                .medicationId(999L)
                .build();

        assertThatThrownBy(() -> reminderService.createReminder(reminderRequest))
                .isInstanceOf(MedicationNotFoundException.class);

        verify(reminderRepository, never()).save(any());
    }

    @Test
    void markAsTaken_success() {
        Reminder existingReminder = Reminder.builder()
                .id(5L)
                .time(LocalTime.now().plusMinutes(10))
                .active(true)
                .taken(false)
                .medication(new Medication())
                .build();

        when(reminderRepository.findById(5L)).thenReturn(Optional.of(existingReminder));
        when(reminderRepository.save(any(Reminder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReminderResponse updatedReminder = reminderService.markAsTaken(5L);

        assertThat(updatedReminder.getTaken()).isTrue();
        verify(reminderRepository).save(existingReminder);
    }

    @Test
    void markAsTaken_notFound_throws() {
        when(reminderRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reminderService.markAsTaken(123L))
                .isInstanceOf(ReminderNotFoundException.class);
    }

    @Test
    void getActiveReminders_mapsToDto() {
        Reminder activeReminder = Reminder.builder()
                .id(1L)
                .time(LocalTime.NOON)
                .active(true)
                .taken(false)
                .medication(new Medication())
                .build();

        when(reminderRepository.findByActiveTrue()).thenReturn(List.of(activeReminder));

        List<ReminderResponse> activeReminders = reminderService.getActiveReminders();

        assertThat(activeReminders).hasSize(1);
        assertThat(activeReminders.get(0).getId()).isEqualTo(1L);
        assertThat(activeReminders.get(0).getTime()).isEqualTo(LocalTime.NOON);
    }

    @Test
    void getTodayReminders_orderedAsc() {
        Reminder morningReminder = Reminder.builder()
                .id(1L)
                .time(LocalTime.of(9, 0))
                .active(true)
                .medication(new Medication())
                .build();

        Reminder laterReminder = Reminder.builder()
                .id(2L)
                .time(LocalTime.of(10, 0))
                .active(true)
                .medication(new Medication())
                .build();

        when(reminderRepository.findByActiveTrueOrderByTimeAsc())
                .thenReturn(List.of(morningReminder, laterReminder));

        List<ReminderResponse> remindersForToday = reminderService.getTodayReminders();

        assertThat(remindersForToday).extracting("id").containsExactly(1L, 2L);
    }
}