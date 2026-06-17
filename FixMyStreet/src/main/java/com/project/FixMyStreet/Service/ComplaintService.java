package com.project.FixMyStreet.Service;


import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Enums.ComplaintPriority;
import com.project.FixMyStreet.Enums.ComplaintStatus;
import com.project.FixMyStreet.Enums.OfficerStatus;
import com.project.FixMyStreet.Models.CitizenProfile;
import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.OfficerProfile;
import com.project.FixMyStreet.Models.User;
import com.project.FixMyStreet.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService
{
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TimelineService timelineService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CitizenProfileRepository citizenProfileRepository;

    @Autowired
    private OfficerProfileRepository officerProfileRepository;
@Autowired
private NotificationService notificationService;

@Autowired
private ImageUploadService imageUploadService;

@Autowired
private EmailService emailService;
    public ApiResponse createComplaint(
            ComplaintRequest request,
            MultipartFile image,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        CitizenProfile citizen =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen profile not found"));

        String  department =

                        request.getDepartment();
        departmentRepository
                .findByNameAndActiveTrue(department)
                .orElseThrow(() ->
                        new RuntimeException("Department not available"));

        OfficerProfile officer =
                getLeastBusyOfficer(
                        department);

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl =
                    imageUploadService
                            .uploadImage(image);
        }

        Complaint complaint =
                Complaint.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .department(request.getDepartment())
                        .imageUrl(imageUrl)

                        .citizenProfileId(
                                citizen.getId())

                        .department(
                                department)

                        .assignedOfficerProfileId(
                                officer.getId())

                        .status(
                                ComplaintStatus.ASSIGNED)

                        .priority(
                                ComplaintPriority.MEDIUM)

                        .address(
                                request.getAddress())

                        .city(
                                request.getCity())

                        .pincode(
                                request.getPincode())

                        .latitude(
                                request.getLatitude())

                        .longitude(
                                request.getLongitude())

                        .createdAt(
                                LocalDateTime.now())

                        .updatedAt(
                                LocalDateTime.now())

                        .assignedAt(
                                LocalDateTime.now())

                        .build();

        complaintRepository.save(
                complaint);

        officer.setTotalAssignedCases(
                officer.getTotalAssignedCases() + 1);

        officer.setCurrentActiveCases(
                officer.getCurrentActiveCases() + 1);

        officerProfileRepository.save(
                officer);

        notificationService.createNotification(
                user.getId(),
                "Complaint Assigned",
                String.format(
                        "Hi Citizen, your complaint '%s' under %s department submitted on %s has been assigned to an officer on %s.",
                        complaint.getTitle(),
                        complaint.getDepartment(),
                        complaint.getCreatedAt(),
                        LocalDateTime.now()
                )
        );
        emailService.sendComplaintAssignedEmail(
                user.getEmail(),
                complaint.getTitle(),
                complaint.getDepartment()
        );
        User officerUser =
                userRepository
                        .findById(officer.getUserId())
                        .orElseThrow();

        notificationService.createNotification(
                officerUser.getId(),
                "New Complaint Assigned",
                String.format(
                        "A new complaint '%s' has been assigned to you.",
                        complaint.getTitle()
                )
        );

        timelineService.addTimelineEntry(
                complaint.getId(),
                "PENDING",
                "Complaint Created",
                "Complaint submitted successfully");

        return new ApiResponse(
                true,
                "Complaint assigned to "
                        + officer.getEmployeeId());
    }

    public List<Complaint> getMyComplaints(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        CitizenProfile citizen =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen profile not found"));

        return complaintRepository
                .findByCitizenProfileId(
                        citizen.getId());
    }

    public List<Complaint> getAssignedComplaints(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        OfficerProfile officer =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Officer profile not found"));

        return complaintRepository
                .findByAssignedOfficerProfileId(
                        officer.getId());
    }

    public Complaint getComplaintById(
            String complaintId) {

        return complaintRepository
                .findById(complaintId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Complaint not found"));
    }




    private OfficerProfile getLeastBusyOfficer(
            String department) {

        List<OfficerProfile> officers =
                officerProfileRepository
                        .findByDepartmentAndStatus(
                                department,
                                OfficerStatus.ACTIVE);

        if (officers.isEmpty()) {
            throw new RuntimeException(
                    "No active officer available in "
                            + department);
        }

        return officers.stream()
                .min(
                        Comparator.comparing(
                                OfficerProfile::getCurrentActiveCases
                        )
                )
                .orElseThrow();
    }

    // Accepting Complaint By the Officer

    public ApiResponse acceptComplaint(
            String complaintId,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        OfficerProfile officer =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        Complaint complaint =
                complaintRepository
                        .findById(complaintId)
                        .orElseThrow();

        if(!officer.getId().equals(
                complaint.getAssignedOfficerProfileId())) {

            throw new RuntimeException(
                    "You are not assigned to this complaint");
        }
        if (complaint.getStatus() != ComplaintStatus.ASSIGNED) {

            return new ApiResponse(
                    false,
                    "Only ASSIGNED complaints can be accepted");
        }
        complaint.setStatus(
                ComplaintStatus.IN_PROGRESS);

        complaint.setUpdatedAt(
                LocalDateTime.now());

        complaintRepository.save(
                complaint);

        CitizenProfile citizen =
                citizenProfileRepository
                        .findById(
                                complaint.getCitizenProfileId())
                        .orElseThrow();

        User citizenUser =
                userRepository
                        .findById(
                                citizen.getUserId())
                        .orElseThrow();

        notificationService.createNotification(
                citizenUser.getId(),
                "Complaint Accepted",
                String.format(
                        "Hi %s, your complaint '%s' under %s department is now being processed. The assigned officer accepted the complaint on %s.",
                        citizen.getName(),
                        complaint.getTitle(),
                        complaint.getDepartment(),
                        LocalDateTime.now()
                )
        );

        emailService.sendComplaintAcceptedEmail(
                citizenUser.getEmail(),
                complaint.getTitle()
        );
        timelineService.addTimelineEntry(
                complaint.getId(),
                "IN_PROGRESS",
                "Complaint Processing Started",
                "Officer "
                        + officer.getEmployeeId()
                        + " accepted the complaint."
        );
        return new ApiResponse(
                true,
                "Complaint accepted");
    }


// Resolving the complaint
    public ApiResponse resolveComplaint(
            String complaintId,
            String remarks,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        OfficerProfile officer =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        Complaint complaint =
                complaintRepository
                        .findById(complaintId)
                        .orElseThrow();


        if(!officer.getId().equals(
                complaint.getAssignedOfficerProfileId())) {

            throw new RuntimeException(
                    "You are not assigned to this complaint");
        }

        if (complaint.getStatus() != ComplaintStatus.IN_PROGRESS) {

            return new ApiResponse(
                    false,
                    "Only IN_PROGRESS complaints can be resolved");
        }

        complaint.setStatus(
                ComplaintStatus.RESOLVED);

        complaint.setRemarks(
                remarks);

        complaint.setResolvedAt(
                LocalDateTime.now());

        complaint.setUpdatedAt(
                LocalDateTime.now());

        complaintRepository.save(
                complaint);

        officer.setCurrentActiveCases(
                Math.max(
                        0,
                        officer.getCurrentActiveCases() - 1
                )
        );

        officer.setTotalResolvedCases(
                officer.getTotalResolvedCases() + 1);

        officerProfileRepository.save(
                officer);
        CitizenProfile citizen =
                citizenProfileRepository
                        .findById(
                                complaint.getCitizenProfileId())
                        .orElseThrow();

        User citizenUser =
                userRepository
                        .findById(
                                citizen.getUserId())
                        .orElseThrow();

        notificationService.createNotification(
                citizenUser.getId(),
                "Complaint Resolved",
                String.format(
                        "Hi %s, your complaint '%s' under %s department has been resolved on %s. Remarks: %s",
                        citizen.getName(),
                        complaint.getTitle(),
                        complaint.getDepartment(),
                        LocalDateTime.now(),
                        remarks
                )
        );

        emailService.sendComplaintResolvedEmail(
                citizenUser.getEmail(),
                complaint.getTitle(),
                remarks
        );
        timelineService.addTimelineEntry(
                complaint.getId(),
                "RESOLVED",
                "Complaint Resolved",
                remarks);

        return new ApiResponse(
                true,
                "Complaint resolved");
    }
// Rejecting the complaint
    public ApiResponse rejectComplaint(
            String complaintId,
            String remarks,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        OfficerProfile officer =
                officerProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        Complaint complaint =
                complaintRepository
                        .findById(complaintId)
                        .orElseThrow();

        if(!officer.getId().equals(
                complaint.getAssignedOfficerProfileId())) {

            throw new RuntimeException(
                    "You are not assigned to this complaint");
        }

        if (complaint.getStatus() != ComplaintStatus.IN_PROGRESS) {

            return new ApiResponse(
                    false,
                    "Only IN_PROGRESS complaints can be rejected");
        }

        complaint.setStatus(
                ComplaintStatus.REJECTED);

        complaint.setRemarks(
                remarks);

        complaint.setUpdatedAt(
                LocalDateTime.now());

        complaintRepository.save(
                complaint);

        officer.setCurrentActiveCases(
                Math.max(
                        0,
                        officer.getCurrentActiveCases() - 1
                )
        );

        officerProfileRepository.save(
                officer);


        CitizenProfile citizen =
                citizenProfileRepository
                        .findById(
                                complaint.getCitizenProfileId())
                        .orElseThrow();

        User citizenUser =
                userRepository
                        .findById(
                                citizen.getUserId())
                        .orElseThrow();

        notificationService.createNotification(
                citizenUser.getId(),
                "Complaint Rejected",
                String.format(
                        "Hi %s, your complaint '%s' under %s department has been rejected on %s. Reason: %s",
                        citizen.getName(),
                        complaint.getTitle(),
                        complaint.getDepartment(),
                        LocalDateTime.now(),
                        remarks
                )
        );

        emailService.sendComplaintRejectedEmail(
                citizenUser.getEmail(),
                complaint.getTitle(),
                remarks
        );
        timelineService.addTimelineEntry(
                complaint.getId(),
                "REJECTED",
                "Complaint Rejected",
                remarks);
        return new ApiResponse(
                true,
                "Complaint rejected");
    }


    public ComplaintStatsResponse getComplaintStats(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        CitizenProfile citizenProfile =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen profile not found"));

        String citizenProfileId =
                citizenProfile.getId();

        long total =
                complaintRepository
                        .countByCitizenProfileId(
                                citizenProfileId);

        long pending =
                complaintRepository
                        .countByCitizenProfileIdAndStatus(
                                citizenProfileId,
                                ComplaintStatus.PENDING);

        long inProgress =
                complaintRepository
                        .countByCitizenProfileIdAndStatus(
                                citizenProfileId,
                                ComplaintStatus.IN_PROGRESS);

        long resolved =
                complaintRepository
                        .countByCitizenProfileIdAndStatus(
                                citizenProfileId,
                                ComplaintStatus.RESOLVED);

        return ComplaintStatsResponse.builder()
                .total(total)
                .pending(pending)
                .inProgress(inProgress)
                .resolved(resolved)
                .build();
    }


    public List<RecentComplaintResponse>
    getRecentComplaints(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        CitizenProfile citizen =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen profile not found"));

        List<Complaint> complaints =
                complaintRepository
                        .findTop5ByCitizenProfileIdOrderByCreatedAtDesc(
                                citizen.getId());

        return complaints.stream()
                .map(complaint ->
                        RecentComplaintResponse.builder()
                                .id(complaint.getId())
                                .title(complaint.getTitle())
                                .status(
                                        complaint.getStatus().name())
                                .createdAt(
                                        complaint.getCreatedAt())
                                .build())
                .toList();
    }


    public ApiResponse submitReview(
            String complaintId,
            ReviewRequest request,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        CitizenProfile citizen =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        Complaint complaint =
                complaintRepository
                        .findById(complaintId)
                        .orElseThrow();

        if (!complaint.getCitizenProfileId()
                .equals(citizen.getId())) {

            return new ApiResponse(
                    false,
                    "Unauthorized");
        }

        if (complaint.getStatus()
                != ComplaintStatus.RESOLVED) {

            return new ApiResponse(
                    false,
                    "Only resolved complaints can be reviewed");
        }

        if (complaint.getRating() != null) {

            return new ApiResponse(
                    false,
                    "Review already submitted");
        }

        if (request.getRating() < 1 ||
                request.getRating() > 5) {

            return new ApiResponse(
                    false,
                    "Rating must be between 1 and 5");
        }

        complaint.setRating(
                request.getRating());

        complaint.setFeedback(
                request.getFeedback());

        complaint.setReviewedAt(
                LocalDateTime.now());

        complaintRepository.save(
                complaint);

        OfficerProfile officer =
                officerProfileRepository
                        .findById(
                                complaint.getAssignedOfficerProfileId()
                        )
                        .orElseThrow();

        User officerUser =
                userRepository
                        .findById(
                                officer.getUserId()
                        )
                        .orElseThrow();

        notificationService.createNotification(

                officerUser.getId(),

                "⭐ Citizen Feedback Received",

                String.format(
                        "The complained person  rated your handling of complaint '%s' with %d stars.\nFeedback: %s",
                        complaint.getTitle(),
                        complaint.getRating(),
                        complaint.getFeedback()
                )
        );


        return new ApiResponse(
                true,
                "Review submitted successfully");
    }


}
