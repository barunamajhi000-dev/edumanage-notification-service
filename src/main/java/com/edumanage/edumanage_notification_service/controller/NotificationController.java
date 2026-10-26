package com.edumanage.edumanage_notification_service.controller;


import com.edumanage.edumanage_notification_service.dto.NotificationHealthResponse;
import com.edumanage.edumanage_notification_service.dto.NotificationRequest;
import com.edumanage.edumanage_notification_service.dto.NotificationResponse;
import com.edumanage.edumanage_notification_service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
        private final NotificationService notificationService;
        public NotificationController(NotificationService notificationService){
            this.notificationService = notificationService;
        }
        //    The controller no longer constructs the response itself;
           //    it delegates that responsibility to the service.

    /*@GetMapping("/health")
    public String checkingHealth(){
        return "Everything looks fine";
    }*/

    @GetMapping("/health")
    public NotificationHealthResponse checkingHealth(){
        return notificationService.getHealthStatus();
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request){

        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @GetMapping
    public java.util.List<NotificationResponse>getAllNotifications(){
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public NotificationResponse getNotificationById(@PathVariable Long id){
        return notificationService.getNotificationById(id);
    }

    @PutMapping("/{id}")
    public NotificationResponse updateNotification(@PathVariable Long id,@Valid @RequestBody NotificationRequest request){
        return notificationService.updateNotification(id,request);
    }
}
