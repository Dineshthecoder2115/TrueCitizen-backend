package com.project.FixMyStreet.Controller;

import com.project.FixMyStreet.Models.Notification;
import com.project.FixMyStreet.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotifications(
            Principal principal) {

        return notificationService
                .getNotifications(
                        principal.getName());
    }

    @PutMapping("/{id}/read")
    public void markAsRead(
            @PathVariable String id,
            Principal principal) {

        notificationService.markAsRead(
                id,
                principal.getName());
    }

    @PutMapping("/read-all")
    public void markAllAsRead(
            Principal principal) {

        notificationService.markAllAsRead(
                principal.getName());
    }
    @GetMapping("/unread-count")
    public long getUnreadCount(
            Principal principal) {

        return notificationService
                .getUnreadCount(
                        principal.getName()
                );
    }
}
