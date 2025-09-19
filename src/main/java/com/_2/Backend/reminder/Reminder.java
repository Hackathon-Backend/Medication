package com._2.Backend.reminder;

import com._2.Backend.medication.Medication;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

    @Entity
    @Table(name = "reminders")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public class Reminder {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalTime time;

        private Boolean active = true;

        private Boolean taken = false;

        @ManyToOne(optional = false)
        @JoinColumn(name = "medication_id")
        private Medication medication;
    }
