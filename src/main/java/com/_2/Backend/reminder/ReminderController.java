package com._2.Backend.reminder;


import com._2.Backend.reminder.dto.ReminderRequest;
import com._2.Backend.reminder.dto.ReminderResponse;
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
@Tag(name = "Reminders", description = "Endpoints for managing reminders")
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderServiceImpl reminderService;

    @Operation(
            summary = "Get all reminders",
            description = "Returns a list of all reminders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reminders retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReminderResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }

    @Operation(
            summary = "Get active reminders",
            description = "Returns only active reminders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active reminders retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReminderResponse.class))))
    })
    @GetMapping("/active")
    public ResponseEntity<List<ReminderResponse>> getActiveReminders() {
        return ResponseEntity.ok(reminderService.getActiveReminders());
    }

    @Operation(
            summary = "Create a new reminder",
            description = "Adds a new reminder linked to a medication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reminder created successfully",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(
            @Valid @RequestBody ReminderRequest request) {

        ReminderResponse newReminder = reminderService.createReminder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReminder);
    }

    @Operation(
            summary = "Mark reminder as taken",
            description = "Marks an existing reminder as taken by the user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reminder marked as taken successfully",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reminder not found", content = @Content)
    })
    @PutMapping("/{id}/taken")
    public ResponseEntity<ReminderResponse> markAsTaken(@PathVariable Long id) {
        ReminderResponse updatedReminder = reminderService.markAsTaken(id);
        return ResponseEntity.ok(updatedReminder);
    }
}