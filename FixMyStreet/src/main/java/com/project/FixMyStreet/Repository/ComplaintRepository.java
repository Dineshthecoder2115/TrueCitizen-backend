package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Enums.ComplaintStatus;
import com.project.FixMyStreet.Models.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint,String> {
    List<Complaint> findByCitizenProfileId(
            String citizenProfileId);

    List<Complaint> findByAssignedOfficerProfileId(
            String officerProfileId);
    long countByStatus(ComplaintStatus status);
    long countByCitizenProfileId(String citizenProfileId);

    long countByCitizenProfileIdAndStatus(
            String citizenProfileId,
            ComplaintStatus status);
    List<Complaint> findTop5ByCitizenProfileIdOrderByCreatedAtDesc(
            String citizenProfileId);
}
