package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.DTO.ApiResponse;
import com.project.FixMyStreet.DTO.DashboardStats;
import com.project.FixMyStreet.DTO.OfficerPerformanceDTO;
import com.project.FixMyStreet.DTO.OfficerProfileResponse;
import com.project.FixMyStreet.Enums.ComplaintStatus;
import com.project.FixMyStreet.Enums.OfficerStatus;
import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.OfficerProfile;
import com.project.FixMyStreet.Models.User;
import com.project.FixMyStreet.Repository.ComplaintRepository;
import com.project.FixMyStreet.Repository.OfficerProfileRepository;
import com.project.FixMyStreet.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    @Autowired
    private  ComplaintRepository complaintRepository;
    @Autowired
     EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private  OfficerProfileRepository officerProfileRepository;
    @Autowired
    private UserRepository userRepository;
    // Get all complaints
    public List<Complaint> getAllComplaints() {

        return complaintRepository.findAll();
    }

    // Get all officers
    public List<OfficerProfile> getAllOfficers() {

        return officerProfileRepository.findAll();
    }

    // Approve officer
    public ApiResponse approveOfficer(String officerId) {

        OfficerProfile officer =
                officerProfileRepository
                        .findById(officerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Officer not found"));

        officer.setStatus(
                OfficerStatus.ACTIVE);

        officerProfileRepository.save(officer);

        User user = userRepository
                .findById(officer.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        notificationService.createNotification(
                user.getId(),
                "Registration Approved",
                "Your officer account has been approved. You can now log in."
        );

        emailService.sendEmail(
                user.getEmail(),
                "Officer Registration Approved",
                "Your registration has been approved. You may now log in."
        );

        return new ApiResponse(
                true,
                "Officer approved successfully");
    }

    // Suspend officer
    public ApiResponse suspendOfficer(String officerId) {

        OfficerProfile officer =
                officerProfileRepository
                        .findById(officerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Officer not found"));

        officer.setStatus(
                OfficerStatus.SUSPENDED);

        officerProfileRepository.save(
                officer);

        return new ApiResponse(
                true,
                "Officer suspended successfully");
    }



    public DashboardStats getDashboardStats() {

        return new DashboardStats(

                complaintRepository.count(),

                complaintRepository.countByStatus(
                        ComplaintStatus.PENDING),

                complaintRepository.countByStatus(
                        ComplaintStatus.ASSIGNED),

                complaintRepository.countByStatus(
                        ComplaintStatus.IN_PROGRESS),

                complaintRepository.countByStatus(
                        ComplaintStatus.RESOLVED),

                complaintRepository.countByStatus(
                        ComplaintStatus.REJECTED),

                officerProfileRepository.count(),

                officerProfileRepository.countByStatus(
                        OfficerStatus.ACTIVE),

                officerProfileRepository.countByStatus(
                        OfficerStatus.SUSPENDED),
               officerProfileRepository.countByStatus(
                        OfficerStatus.PENDING),

                officerProfileRepository.countByStatus(
                        OfficerStatus.REJECTED)
        );
    }

    public List<OfficerPerformanceDTO>
    getOfficerPerformance() {

        List<OfficerProfile> officers =
                officerProfileRepository.findAll();

        return officers.stream()
                .map(officer -> {

                    double resolutionRate = 0;

                    if(officer.getTotalAssignedCases() > 0) {

                        resolutionRate =
                                ((double)
                                        officer.getTotalResolvedCases()
                                        /
                                        officer.getTotalAssignedCases())
                                        * 100;
                    }

                    return new OfficerPerformanceDTO(
                            officer.getId(),
                            officer.getEmployeeId(),
                            officer.getDesignation(),
                            officer.getTotalAssignedCases(),
                            officer.getTotalResolvedCases(),
                            officer.getCurrentActiveCases(),
                            resolutionRate
                    );

                })
                .toList();
    }
    public List<OfficerProfile> getPendingOfficers() {

        return officerProfileRepository
                .findByStatus(
                        OfficerStatus.PENDING
                );
    }
    public ApiResponse rejectOfficer(
            String officerId) {

        OfficerProfile officer =
                officerProfileRepository
                        .findById(officerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Officer not found"));

        officer.setStatus(
                OfficerStatus.REJECTED);

        officerProfileRepository.save(officer);

        User user = userRepository
                .findById(officer.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        notificationService.createNotification(
                user.getId(),
                "Registration Rejected",
                "Your officer registration has been rejected."
        );

        emailService.sendEmail(
                user.getEmail(),
                "Officer Registration Rejected",
                "Your registration has been rejected. Please contact administration."
        );

        return new ApiResponse(
                true,
                "Officer rejected");
    }

    public List<OfficerProfileResponse> getPendingOfficerDetails() {

        List<OfficerProfile> officers =
                officerProfileRepository.findByStatus(
                        OfficerStatus.PENDING);

        return officers.stream()
                .map(officer -> {

                    User user = userRepository
                            .findById(officer.getUserId())
                            .orElseThrow();

                    return new OfficerProfileResponse(
                            officer.getId(),
                            officer.getEmployeeId(),
                            officer.getDesignation(),
                            officer.getDepartment(),
                            officer.getOfficeAddress(),
                            officer.getYearsOfExperience(),
                            officer.getIdCardUrl(),
                            officer.getTotalAssignedCases(),
                            officer.getTotalResolvedCases(),
                            officer.getCurrentActiveCases(),
                            user.getPhoneNumber(),
                            user.getName(),
                            user.getEmail()
                    );
                })
                .toList();
    }
}