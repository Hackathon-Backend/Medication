package com._2.Backend.medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

 
 
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByStatusAndActiveTrueOrderByTimeToTakeAsc(Medication.Status status);

    List<Medication> findByStatusOrderByTimeToTakeDesc(Medication.Status status);

    List<Medication> findByActive(boolean active);
     
    List<Medication> findByStatus(Medication.Status status);
     
    List<Medication> findByTimeToTake(LocalTime timeToTake);

    List<Medication> findByName(String name);

    List<Medication> findByNameContainingIgnoreCase(String name);
}