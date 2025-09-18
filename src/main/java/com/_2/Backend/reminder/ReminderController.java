package com._2.Backend.reminder;


import com._2.Backend.reminder.dtos.ReminderRequest;
import com._2.Backend.reminder.dtos.ReminderResponse;
import com._2.Backend.reminder.services.ReminderService;
import com._2.Backend.reminder.services.ReminderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recordatorios", description = "Endpoints para gestionar recordatorios")
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    @Operation(
            summary = "Obtener todos los recordatorios",
            description = "Devuelve una lista con todos los recordatorios registrados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recordatorios obtenidos correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReminderResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }

    @Operation(
            summary = "Obtener recordatorios activos",
            description = "Devuelve únicamente los recordatorios que están activos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recordatorios activos obtenidos correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReminderResponse.class))))
    })
    @GetMapping("/active")
    public ResponseEntity<List<ReminderResponse>> getActiveReminders() {
        return ResponseEntity.ok(reminderService.getActiveReminders());
    }

    @Operation(
            summary = "Crear un nuevo recordatorio",
            description = "Agrega un nuevo recordatorio vinculado a un medicamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recordatorio creado correctamente",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(
            @Valid @RequestBody ReminderRequest request) {

        ReminderResponse newReminder = reminderService.createReminder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReminder);
    }

    @Operation(
            summary = "Marcar recordatorio como tomado",
            description = "Marca un recordatorio existente como tomado por el usuario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recordatorio marcado como tomado correctamente",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recordatorio no encontrado", content = @Content)
    })
    @PutMapping("/{id}/taken")
    public ResponseEntity<ReminderResponse> markAsTaken(@PathVariable Long id) {
        ReminderResponse updatedReminder = reminderService.markAsTaken(id);
        return ResponseEntity.ok(updatedReminder);
    }
}