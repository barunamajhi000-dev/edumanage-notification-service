
package com.edumanage.edumanage_notification_service.controller;

import com.edumanage.edumanage_notification_service.dto.NotificationHealthResponse;
import com.edumanage.edumanage_notification_service.dto.NotificationRequest;
import com.edumanage.edumanage_notification_service.dto.NotificationResponse;
import com.edumanage.edumanage_notification_service.service.NotificationService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void healthEndpointShouldReturnOk() throws Exception {

        when(notificationService.getHealthStatus())
                .thenReturn(new NotificationHealthResponse(
                        "edumanage-notification-service",
                        "UP",
                        "Service is running"
                ));

        mockMvc.perform(get("/api/notifications/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.service")
                        .value("edumanage-notification-service"))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("Service is running"));
    }

    @Test
    void createNotificationShouldReturnAccepted() throws Exception {

        NotificationResponse response = new NotificationResponse(
                1L,
                "student@example.com",
                "Enrollment confirmed",
                "PENDING",
                LocalDateTime.now()
        );

        when(notificationService.createNotification(
                org.mockito.ArgumentMatchers.any(NotificationRequest.class)
        )).thenReturn(response);


        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "recipient": "student@example.com",
                                    "message": "Enrollment confirmed"
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.recipient")
                        .value("student@example.com"))
                .andExpect(jsonPath("$.status").value("PENDING"));


    }

}
