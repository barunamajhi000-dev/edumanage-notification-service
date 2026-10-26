
package com.edumanage.edumanage_notification_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NotificationUpdateRequest(

        @NotBlank
        @Email
        String recipient,

        @NotBlank
        String message
) {}
