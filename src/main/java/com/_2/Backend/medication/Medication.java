package com._2.Backend.medication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID autogenerado

    @NotNull
    private String name; // Nombre del medicamento

    @NotNull
    private String dose; // Dosis a tomar

    @NotNull
    private String frequency; // Frecuencia de toma

    @NotNull
    private LocalTime timeToTake; // Hora programada para tomar el medicamento

    private boolean active = true; // Si el medicamento está activo o no
    private boolean taken = false; // Si el medicamento ya ha sido tomado

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // Estado inicial

    public enum Status {
        PENDING,
        TAKEN
    }

    // Constructor vacío requerido por JPA
    public Medication() {}

    // Constructor con campos obligatorios
    public Medication(String name, String dose, String frequency, LocalTime timeToTake) {
        this.name = name;
        this.dose = dose;
        this.frequency = frequency;
        this.timeToTake = timeToTake;
        this.active = true;
        this.taken = false;
        this.status = Status.PENDING;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public LocalTime getTimeToTake() { return timeToTake; }
    public void setTimeToTake(LocalTime timeToTake) { this.timeToTake = timeToTake; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isTaken() { return taken; }
    public void setTaken(boolean taken) { this.taken = taken; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}