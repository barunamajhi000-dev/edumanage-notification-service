package com.edumanage.edumanage_notification_service.dto;

public record NotificationHealthResponse(
        String service,
        String status,
        String message
) {
}
