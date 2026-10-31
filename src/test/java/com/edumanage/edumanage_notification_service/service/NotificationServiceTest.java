
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

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.edumanage.edumanage_notification_service.dto.NotificationUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // Arrange
        Long notificationId = 999L;

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.getNotificationById(notificationId)
        );
    }

//    @Test
//    void updateNotificationShouldUpdateAndReturnNotification() {
//
//        // Arrange
//        Long notificationId = 1L;
//
//        Notification existingNotification = new Notification();
//        existingNotification.setRecipient("old@example.com");
//        existingNotification.setMessage("Old message");
//        existingNotification.setStatus("PENDING");
//
//        NotificationRequest request = new NotificationRequest(
//                "new@example.com",
//                "Updated enrollment message"
//        );
//
//        when(notificationRepository.findById(notificationId))
//                .thenReturn(Optional.of(existingNotification));
//
//        ArgumentCaptor<Notification> captor =
//                ArgumentCaptor.forClass(Notification.class);
//
//        verify(notificationRepository).save(captor.capture());
//
//        Notification savedNotification = captor.getValue();
//
//        assertEquals("student@example.com", savedNotification.getRecipient());
//        assertEquals("Your enrollment is confirmed.", savedNotification.getMessage());
//        assertEquals("PENDING", savedNotification.getStatus());
//
//        // Act
//        NotificationResponse response =
//                notificationService.updateNotification(notificationId, request);
//
//        // Assert
//        assertEquals("new@example.com", response.recipient());
//        assertEquals("Updated enrollment message", response.message());
//        assertEquals("PENDING", response.status());
//
//        verify(notificationRepository).findById(notificationId);
//        verify(notificationRepository, times(1))
//                .save(existingNotification);
//    }


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


    @Test
    void deleteNotificationShouldThrowExceptionWhenNotFound() {

        // Arrange
        Long notificationId = 999L;

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.deleteNotification(notificationId)
        );

        // Verify repository lookup occurred
        verify(notificationRepository).findById(notificationId);

        // Verify deletion was never attempted
        verify(notificationRepository, never()).delete(any(Notification.class));
    }


    @Test
    void getAllNotificationsShouldReturnPaginatedResponses() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 2);

        Notification first = new Notification();
        first.setRecipient("first@example.com");
        first.setMessage("First notification");
        first.setStatus("PENDING");

        Notification second = new Notification();
        second.setRecipient("second@example.com");
        second.setMessage("Second notification");
        second.setStatus("PENDING");

        Page<Notification> notificationPage = new PageImpl<>(
                List.of(first, second),
                pageable,
                5
        );

        when(notificationRepository.findAll(pageable))
                .thenReturn(notificationPage);

        // Act
        Page<NotificationResponse> result =
                notificationService.getAllNotifications(pageable);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertEquals("first@example.com",
                result.getContent().get(0).recipient());
        assertEquals("second@example.com",
                result.getContent().get(1).recipient());

        verify(notificationRepository).findAll(pageable);
    }




}
