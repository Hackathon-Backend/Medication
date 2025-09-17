package com._2.Backend.medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

//Interfaz que define el repositorio para la entidad Medication extiende JpaRepository para heredar métodos CRUD automaticamente//
 
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    
    //Metodo Principal medicación por tomar//

    List<Medication> findByStatusAndActiveTrueOrderByTimeToTakeAsc(Medication.Status status);

    //Metodo para la medicacion ya tomada//

     List<Medication> findByStatusOrderByTimeToTakeDesc(Medication.Status status);

    //Método para buscar medicamentos activos Spring Data JPA genera automáticamente la consulta SQL correspondiente//

    List<Medication> findByActive(boolean active);

    //Método para buscar medicamentos por estado (PENDING o TAKEN)//
     
    List<Medication> findByStatus(Medication.Status status);

    //Método para buscar medicamentos que se toman a una hora específica//
     
    List<Medication> findByTimeToTake(LocalTime timeToTake);

    
    //Metodo para buscar medicamentos por nombre exacto//

    List<Medication> findByName(String name);

    //Buscar medicamentos por nombre parcial, ignorando mayúsculas/minúsculas//

    List<Medication> findByNameContainingIgnoreCase(String name);
}