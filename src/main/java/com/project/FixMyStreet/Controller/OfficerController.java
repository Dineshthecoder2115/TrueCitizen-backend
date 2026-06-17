package com.project.FixMyStreet.Controller;

import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.OfficerProfile;
import com.project.FixMyStreet.Service.ComplaintService;
import com.project.FixMyStreet.Service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/officer")
@RequiredArgsConstructor
public class OfficerController {

    @Autowired
    private OfficerService officerService;
    private final ComplaintService complaintService;

    @GetMapping("/complaints")
    public List<Complaint> getAssignedComplaints(
            Principal principal) {

        return complaintService
                .getAssignedComplaints(
                        principal.getName());
    }

    @GetMapping("/complaints/{id}")
    public Complaint getComplaintById(
            @PathVariable String id,
            Principal principal) {

        return officerService
                .getComplaintById(
                        id,
                        principal.getName());
    }

    @PutMapping("/complaints/{id}/accept")
    public ApiResponse acceptComplaint(
            @PathVariable String id,
            Principal principal) {

        return complaintService
                .acceptComplaint(
                        id,
                        principal.getName());
    }

    @PutMapping("/complaints/{id}/reject")
    public ApiResponse rejectComplaint(
            @PathVariable String id,
            @RequestBody RejectComplaintRequest request,
            Principal principal) {

        return complaintService
                .rejectComplaint(
                        id,
                        request.getRemarks(),
                        principal.getName());
    }

    @PutMapping("/complaints/{id}/resolve")
    public ApiResponse resolveComplaint(
            @PathVariable String id,
            @RequestBody RejectComplaintRequest request,
            Principal principal) {

        return complaintService.resolveComplaint(
                id,
                request.getRemarks(),
                principal.getName());
    }

    @GetMapping("/stats")
    public OfficerStatsResponse getStats(
            Principal principal) {

        return officerService
                .getOfficerStats(
                        principal.getName());
    }


    @GetMapping("/profile")
    public OfficerProfileResponse getProfile(
            Principal principal) {

        return officerService.getProfile(
                principal.getName());
    }

    @GetMapping("/activities")
    public List<RecentActivityResponse>
    getRecentActivities(
            Principal principal) {

        return officerService
                .getRecentActivities(
                        principal.getName());
    }

    @PutMapping("/profile")
    public ApiResponse updateProfile(

            @RequestBody
            UpdateOfficerProfileRequest request,

            Principal principal) {

        return officerService.updateProfile(
                request,
                principal.getName());
    }
}