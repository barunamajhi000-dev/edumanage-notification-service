package com.edumanage.edumanage_notification_service.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String recipient,
        String message,
        String status,
        LocalDateTime createdAt
) {
}
