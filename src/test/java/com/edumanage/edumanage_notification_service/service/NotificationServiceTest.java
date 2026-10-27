
package com.edumanage.edumanage_notification_service.service;

import com.edumanage.edumanage_notification_service.dto.NotificationRequest;
import com.edumanage.edumanage_notification_service.dto.NotificationResponse;
import com.edumanage.edumanage_notification_service.entity.Notification;
import com.edumanage.edumanage_notification_service.repository.NotificationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.edumanage.edumanage_notification_service.exception.NotificationNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edumanage.edumanage_notification_service.dto.NotificationUpdateRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotificationShouldSaveAndReturnNotification() {

        // Arrange: prepare the request
        NotificationRequest request = new NotificationRequest(
                "student@example.com",
                "Your enrollment is confirmed."
        );

        // Mock the repository save operation
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: execute the service method
        NotificationResponse response =
                notificationService.createNotification(request);

        // Assert: verify the returned response
        assertEquals("student@example.com", response.recipient());
        assertEquals("Your enrollment is confirmed.", response.message());
        assertEquals("PENDING", response.status());

        // Verify the entity sent to the repository
        ArgumentCaptor<Notification> captor =
                ArgumentCaptor.forClass(Notification.class);

        verify(notificationRepository).save(captor.capture());

        Notification savedNotification = captor.getValue();

        assertEquals("student@example.com", savedNotification.getRecipient());
        assertEquals("Your enrollment is confirmed.", savedNotification.getMessage());
        assertEquals("PENDING", savedNotification.getStatus());
    }
    @Test
    void getNotificationByIdShouldThrowExceptionWhenNotFound() {

        // Arrange
        Long notificationId = 999L;

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.getNotificationById(notificationId)
        );

        verify(notificationRepository).findById(notificationId);
    }

    @Test
    void updateNotificationShouldUpdateAndReturnNotification() {

        // Arrange
        Long notificationId = 1L;

        Notification existingNotification = new Notification();
        existingNotification.setRecipient("old@example.com");
        existingNotification.setMessage("Old message");
        existingNotification.setStatus("PENDING");

        NotificationRequest request = new NotificationRequest(
                "new@example.com",
                "Updated enrollment message"
        );

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(existingNotification));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        NotificationResponse response =
                notificationService.updateNotification(notificationId, request);

        // Assert
        assertEquals("new@example.com", response.recipient());
        assertEquals("Updated enrollment message", response.message());
        assertEquals("PENDING", response.status());

        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository, times(1))
                .save(existingNotification);
    }


    @Test
    void deleteNotificationShouldDeleteExistingNotification() {

        // Arrange
        Long notificationId = 1L;

        Notification existingNotification = new Notification();
        existingNotification.setRecipient("student@example.com");
        existingNotification.setMessage("Enrollment confirmed");
        existingNotification.setStatus("PENDING");

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(existingNotification));

        // Act
        notificationService.deleteNotification(notificationId);

        // Assert
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).delete(existingNotification);
    }

}
