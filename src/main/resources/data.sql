INSERT INTO medications (name, dose, frequency, time_to_take, interval_hours, interval_days, active) VALUES
('Ibuprofen', '200mg', 'ONCE_A_DAY', '08:00:00', NULL, NULL, TRUE),
('Paracetamol', '500mg', 'EVERY_EIGHT_HOURS', '14:00:00', NULL, NULL, TRUE),
('Vitamin C', '1000mg', 'THREE_TIMES_A_DAY', '09:30:00', NULL, NULL, TRUE);
INSERT INTO reminders (time, active, taken, medication_id) VALUES
('08:00:00', TRUE, FALSE, 1),
('14:00:00', TRUE, FALSE, 2),
('09:30:00', TRUE, TRUE, 3);