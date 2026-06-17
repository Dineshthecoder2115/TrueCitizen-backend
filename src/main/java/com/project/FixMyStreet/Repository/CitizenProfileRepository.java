package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Models.CitizenProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CitizenProfileRepository extends MongoRepository<CitizenProfile,String> {
    Optional<CitizenProfile> findByUserId(String userId);
}
