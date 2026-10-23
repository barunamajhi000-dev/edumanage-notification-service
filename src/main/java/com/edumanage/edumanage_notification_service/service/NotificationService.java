package com.edumanage.edumanage_notification_service.service;

import com.edumanage.edumanage_notification_service.dto.NotificationHealthResponse;
import com.edumanage.edumanage_notification_service.dto.NotificationRequest;
import com.edumanage.edumanage_notification_service.dto.NotificationResponse;
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

    public NotificationResponse createNotification(NotificationRequest request){
        return new NotificationResponse(
                request.recipient(),
                request.message(),
                "Accepted"
        );
    }
}