package com._2.Backend.reminder.Service;

public interface ReminderService {
    List<ReminderResponse> getAllReminders();
    List<ReminderResponse> getActiveReminders();
    ReminderResponse createReminder(ReminderRequest request);
    ReminderResponse markAsTaken(Long id);
}
