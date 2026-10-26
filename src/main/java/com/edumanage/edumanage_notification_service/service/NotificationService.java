package com.edumanage.edumanage_notification_service.service;

import com.edumanage.edumanage_notification_service.dto.NotificationHealthResponse;
import com.edumanage.edumanage_notification_service.dto.NotificationRequest;
import com.edumanage.edumanage_notification_service.dto.NotificationResponse;
import com.edumanage.edumanage_notification_service.entity.Notification;
import com.edumanage.edumanage_notification_service.repository.NotificationRepository;
import com.edumanage.edumanage_notification_service.exception.NotificationNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;


    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public NotificationHealthResponse getHealthStatus() {
        return new NotificationHealthResponse(
                "EduManage Notification Service",
                "UP",
                "Everything looks fine"
        );
    }


    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {

        Notification notification = new Notification();
        notification.setRecipient(request.recipient());
        notification.setMessage(request.message());
        notification.setStatus("PENDING");

        Notification savedNotification =
                notificationRepository.save(notification);

        return new NotificationResponse(
                savedNotification.getId(),
                savedNotification.getRecipient(),
                savedNotification.getMessage(),
                savedNotification.getStatus(),
                savedNotification.getCreatedAt()
        );
    }

    @Transactional
    public java.util.List<NotificationResponse> getAllNotifications(){
        return notificationRepository.findAll()
                .stream()
                .map(notification -> new NotificationResponse(
                        notification.getId(),
                        notification.getRecipient(),
                        notification.getMessage(),
                        notification.getStatus(),
                        notification.getCreatedAt()
                )).toList();
    }

    @Transactional
    public NotificationResponse getNotificationById(Long id){

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(()->new NotificationNotFoundException(
                        "Notification not found with this id : "+id
                ));
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipient(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getCreatedAt()
        );
    }

    @Transactional
    public NotificationResponse updateNotification(Long id,NotificationRequest request){

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(()->new NotificationNotFoundException(
                        "Notification not found with is id : " +id
                ));

        notification.setRecipient(request.recipient());
        notification.setMessage(request.message());

        Notification updateNotification = notificationRepository.save(notification);

        return new NotificationResponse(
                updateNotification.getId(),
                updateNotification.getRecipient(),
                updateNotification.getMessage(),
                updateNotification.getStatus(),
                updateNotification.getCreatedAt()
        );
    }

}