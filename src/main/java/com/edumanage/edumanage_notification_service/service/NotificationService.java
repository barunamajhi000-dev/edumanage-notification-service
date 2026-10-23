package com.edumanage.edumanage_notification_service.service;

import com.edumanage.edumanage_notification_service.dto.NotificationHealthResponse;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public NotificationHealthResponse getHealthStatus() {
        return new NotificationHealthResponse(
                "EduManage Notification Service",
                "UP",
                "Everything looks fine"
        );
    }
}