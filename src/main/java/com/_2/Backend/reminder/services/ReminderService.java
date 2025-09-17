package com._2.Backend.reminder;

import com._2.Backend.reminder.dto.ReminderRequest;
import com._2.Backend.reminder.dto.ReminderResponse;

import java.util.List;

public interface ReminderService {
    List<ReminderResponse> getAllReminders();
    List<ReminderResponse> getActiveReminders();
    ReminderResponse createReminder(ReminderRequest request);
    ReminderResponse markAsTaken(Long id);
}
