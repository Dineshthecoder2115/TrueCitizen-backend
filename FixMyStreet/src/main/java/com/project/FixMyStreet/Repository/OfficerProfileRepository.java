package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Enums.OfficerStatus;
import com.project.FixMyStreet.Models.OfficerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OfficerProfileRepository extends MongoRepository<OfficerProfile,String> {
    List<OfficerProfile> findByDepartmentAndStatus(
            String  department,
            OfficerStatus status);

    Optional<OfficerProfile> findByUserId(
            String userId);

    long countByStatus(OfficerStatus status);
    boolean existsByEmployeeId(String employeeId);
    List<OfficerProfile> findByStatus(
            OfficerStatus status
    );
}
