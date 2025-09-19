package com._2.Backend.reminder;

import com._2.Backend.reminder.dtos.ReminderRequest;
import com._2.Backend.reminder.dtos.ReminderResponse;
import com._2.Backend.reminder.exceptions.InvalidReminderTimeException;
import com._2.Backend.reminder.exceptions.ReminderNotFoundException;
import com._2.Backend.reminder.services.ReminderService;
import com._2.Backend.security.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReminderController.class)
@Import(GlobalExceptionHandler.class)
class ReminderControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // замоканный сервис
    private ReminderService reminderService;

    private ReminderResponse reminderResponse;

    @BeforeEach
    void setUp() {
        reminderResponse = ReminderResponse.builder()
                .id(1L)
                .time(LocalTime.of(9, 0))
                .active(true)
                .taken(false)
                .medicationName("Ibuprofeno")
                .build();
    }

    @Test
    void getAllReminders_returnsList() throws Exception {
        given(reminderService.getAllReminders()).willReturn(List.of(reminderResponse));

        mockMvc.perform(get("/api/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getActiveReminders_returnsList() throws Exception {
        given(reminderService.getActiveReminders()).willReturn(List.of(reminderResponse));

        mockMvc.perform(get("/api/reminders/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void createReminder_returnsCreated() throws Exception {
        ReminderRequest request = ReminderRequest.builder()
                .time(LocalTime.of(10, 0))
                .active(true)
                .medicationId(10L)
                .build();

        given(reminderService.createReminder(any(ReminderRequest.class)))
                .willReturn(reminderResponse);

        mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.medicationName").value("Ibuprofeno"));
    }

    @Test
    void markAsTaken_returnsUpdated() throws Exception {
        given(reminderService.markAsTaken(1L)).willReturn(reminderResponse);

        mockMvc.perform(put("/api/reminders/1/taken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createReminder_withPastTime_returnsBadRequest() throws Exception {
        ReminderRequest request = ReminderRequest.builder()
                .time(LocalTime.now().minusMinutes(5))
                .active(true)
                .medicationId(10L)
                .build();

        given(reminderService.createReminder(any(ReminderRequest.class)))
                .willThrow(new InvalidReminderTimeException("La hora no puede ser nula ni estar en el pasado"));

        mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("DATOS_INVÁLIDOS"));
    }

    @Test
    void markAsTaken_notFound_returnsNotFound() throws Exception {
        doThrow(new ReminderNotFoundException(99L)).when(reminderService).markAsTaken(99L);

        mockMvc.perform(put("/api/reminders/99/taken"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NO_ENCONTRADO"));
    }
    @TestConfiguration
    static class NoSecurityConfig {
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
}
}