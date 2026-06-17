package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Enums.ComplaintStatus;
import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.OfficerProfile;
import com.project.FixMyStreet.Models.User;
import com.project.FixMyStreet.Repository.ComplaintRepository;
import com.project.FixMyStreet.Repository.OfficerProfileRepository;
import com.project.FixMyStreet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Service
public class OfficerService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OfficerProfileRepository officerProfileRepository;

    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ComplaintRepository complaintRepository;


public OfficerStatsResponse getOfficerStats(
        String email) {
    User user =
            userRepository
                    .findByEmail(email)
                    .orElseThrow();

    OfficerProfile officer =
            officerProfileRepository
                    .findByUserId(user.getId())
                    .orElseThrow();

    List<Complaint> complaints =
            complaintRepository
                    .findByAssignedOfficerProfileId(
                            officer.getId());

    long assigned =
            complaints.size();

    long inProgress =
            complaints.stream()
                    .filter(c ->
                            c.getStatus()
                                    == ComplaintStatus.IN_PROGRESS)
                    .count();

    long resolved =
            complaints.stream()
                    .filter(c ->
                            c.getStatus()
                                    == ComplaintStatus.RESOLVED)
                    .count();

    long rejected =
            complaints.stream()
                    .filter(c ->
                            c.getStatus()
                                    == ComplaintStatus.REJECTED)
                    .count();

    return new OfficerStatsResponse(
            assigned,
            inProgress,
            resolved,
            rejected
    );
}

    public OfficerProfileResponse getProfile(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        OfficerProfile profile =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        return new OfficerProfileResponse(
                profile.getId(),                profile.getEmployeeId(),
                profile.getDesignation(),
                profile.getDepartment(),
                profile.getOfficeAddress(),
                profile.getYearsOfExperience(),
                profile.getIdCardUrl(),
                profile.getTotalAssignedCases(),
                profile.getTotalResolvedCases(),
                profile.getCurrentActiveCases(),
                user.getPhoneNumber(),
                user.getName(),
                user.getEmail()
        );
    }


    public List<RecentActivityResponse>
    getRecentActivities(String email) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        OfficerProfile officer =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        return complaintRepository
                .findByAssignedOfficerProfileId(
                        officer.getId())
                .stream()
                .sorted((a, b) ->
                        b.getUpdatedAt()
                                .compareTo(a.getUpdatedAt()))
                .limit(5)
                .map(c -> new RecentActivityResponse(
                        c.getTitle(),
                        c.getStatus().name(),
                        c.getUpdatedAt()))
                .toList();
    }

    public ApiResponse updateProfile(
            UpdateOfficerProfileRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);

        OfficerProfile profile =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Officer profile not found"));

        profile.setOfficeAddress(request.getOfficeAddress());

        profile.setYearsOfExperience(
                request.getYearsOfExperience());

        officerProfileRepository.save(profile);

        return new ApiResponse(
                true,
                "Profile updated successfully");
    }


    public Complaint getComplaintById(
            String complaintId,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        OfficerProfile officer =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Officer not found"));

        Complaint complaint =
                complaintRepository
                        .findById(complaintId)
                        .orElseThrow(() ->
                                new RuntimeException("Complaint not found"));

        if (!officer.getId().equals(
                complaint.getAssignedOfficerProfileId())) {

            throw new RuntimeException(
                    "You are not assigned to this complaint");
        }

        return complaint;
    }

}