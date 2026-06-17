package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.Notification;
import com.project.FixMyStreet.Models.User;
import com.project.FixMyStreet.Repository.NotificationRepository;
import com.project.FixMyStreet.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    public void createNotification(
            String userId,
            String title,
            String message) {

        Notification notification =
                Notification.builder()
                        .userId(userId)
                        .title(title)
                        .message(message)
                        .read(false)
                        .createdAt(LocalDateTime.now())
                        .build();

        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        user.getId());
    }

    public void markAsRead(
            String notificationId,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow();

        if (!notification.getUserId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Unauthorized");
        }

        notification.setRead(true);

        notificationRepository.save(
                notification);
    }

    public void markAllAsRead(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdOrderByCreatedAtDesc(
                                user.getId());

        notifications.forEach(n ->
                n.setRead(true));

        notificationRepository.saveAll(
                notifications);
    }

    public void notifyCitizen(
            User citizen,
            Complaint complaint,
            String action) {

        String message = String.format(
                "Hi %s, your complaint '%s' under %s department, submitted on %s, has been %s on %s.",
                citizen.getName(),
                complaint.getTitle(),
                complaint.getDepartment(),
                complaint.getCreatedAt(),
                action,
                LocalDateTime.now()
        );

        createNotification(
                citizen.getId(),
                "Complaint Update",
                message
        );
    }

    public long getUnreadCount(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        return notificationRepository
                .countByUserIdAndReadFalse(
                        user.getId()
                );
    }
}