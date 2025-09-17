package com._2.Backend.medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalTime;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByActive(boolean active);
    List<Medication> findByTimeToTake(LocalTime timeToTake);
    List<Medication> findByName(String name);
    List<Medication> findByNameContainingIgnoreCase(String name);
    List<Medication> findByTakenFalseAndActiveTrueOrderByTimeToTakeAsc();
    List<Medication> findByTakenTrueOrderByTimeToTakeDesc();
}