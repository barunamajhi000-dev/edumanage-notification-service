package com.edumanage.edumanage_notification_service.controller;


import com.edumanage.edumanage_notification_service.dto.NotificationHealthResponse;
import com.edumanage.edumanage_notification_service.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
