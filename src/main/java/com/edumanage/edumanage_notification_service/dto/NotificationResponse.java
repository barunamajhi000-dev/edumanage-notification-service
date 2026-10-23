package com.edumanage.edumanage_notification_service.dto;

public record NotificationResponse(
        String recipient,
        String message,
        String status
) {
}
