package com.project.FixMyStreet.Controller;

import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.ComplaintTimeline;
import com.project.FixMyStreet.Service.AdminService;
import com.project.FixMyStreet.Service.ComplaintService;
import com.project.FixMyStreet.Service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    @Autowired
    private  ComplaintService complaintService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TimelineService timelineService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse createComplaint(

            @ModelAttribute ComplaintRequest request,

            @RequestParam(required = false)
            MultipartFile image,

            Principal principal
    ) {

        return complaintService.createComplaint(
                request,
                image,
                principal.getName());
    }

    @GetMapping("/whoami")
    public String whoAmI(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/my")
    public List<Complaint> getMyComplaints(
            Principal principal) {

        return complaintService
                .getMyComplaints(
                        principal.getName());
    }
    @GetMapping("/{id}")
    public Complaint getComplaintById(
            @PathVariable String id) {

        return complaintService
                .getComplaintById(id);
    }

    @GetMapping("/stats")
    public ResponseEntity<ComplaintStatsResponse>
    getComplaintStats(
            Authentication authentication) {

        return ResponseEntity.ok(
                complaintService.getComplaintStats(
                        authentication.getName()
                )
        );
    }



    @GetMapping("/recent")
    public List<RecentComplaintResponse>
    getRecentComplaints(
            Principal principal) {

        return complaintService
                .getRecentComplaints(
                        principal.getName());
    }

    @PostMapping("/{id}/review")
    public ApiResponse submitReview(
            @PathVariable String id,
            @RequestBody ReviewRequest request,
            Principal principal) {

        return complaintService.submitReview(
                id,
                request,
                principal.getName());
    }
    @GetMapping("timeline/{complaintId}")
    public List<ComplaintTimeline> getTimeline(
            @PathVariable String complaintId) {

        return timelineService.getTimeline(
                complaintId
        );
    }


}
