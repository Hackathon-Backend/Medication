INSERT INTO medications (id, name, dose, frequency, time_to_take, active) VALUES
(1, 'Ibuprofen', '200mg', 'ONCE_A_DAY', '08:00:00', TRUE),
(2, 'Paracetamol', '500mg', 'EVERY_8_HOURS', '14:00:00', TRUE),
(3, 'Vitamin C', '1000mg', 'WEEKLY', '09:30:00', TRUE);
INSERT INTO reminders (id, time, active, taken, medication_id) VALUES
(1, '08:00:00', TRUE, FALSE, 1),
(2, '14:00:00', TRUE, FALSE, 2),
(3, '09:30:00', TRUE, TRUE, 3);