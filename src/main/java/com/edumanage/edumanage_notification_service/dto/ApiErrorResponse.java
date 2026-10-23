package com.edumanage.edumanage_notification_service.dto;

public record ApiErrorResponse(
        int status,
        String error,
        String message
) {
}
