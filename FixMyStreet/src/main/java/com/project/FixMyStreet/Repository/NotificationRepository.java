package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification,String> {


        List<Notification> findByUserIdOrderByCreatedAtDesc(
                String userId);
    long countByUserIdAndReadFalse(String userId);

}
