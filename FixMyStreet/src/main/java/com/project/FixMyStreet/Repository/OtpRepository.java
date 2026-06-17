package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Models.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<OTP,String> {
    Optional<OTP> findByEmail(String email);

}
