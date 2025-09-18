package com._2.Backend.reminder.services;

import com._2.Backend.reminder.dtos.ReminderRequest;
import com._2.Backend.reminder.dtos.ReminderResponse;

import java.util.List;

public interface ReminderService {
    List<ReminderResponse> getAllReminders();
    List<ReminderResponse> getActiveReminders();
    List<ReminderResponse> getTodayReminders();
    ReminderResponse createReminder(ReminderRequest request);
    ReminderResponse markAsTaken(Long id);
}
