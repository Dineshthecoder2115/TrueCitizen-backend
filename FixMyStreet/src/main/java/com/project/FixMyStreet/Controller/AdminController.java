package com.project.FixMyStreet.Controller;


import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Models.Complaint;
import com.project.FixMyStreet.Models.OfficerProfile;
import com.project.FixMyStreet.Repository.DepartmentRepository;
import com.project.FixMyStreet.Service.AdminService;
import com.project.FixMyStreet.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.project.FixMyStreet.Models.Department;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Autowired
    private DepartmentService departmentService;
    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints() {

        return adminService.getAllComplaints();
    }

    @GetMapping("/officers")
    public List<OfficerProfile> getAllOfficers() {

        return adminService.getAllOfficers();
    }

    @PutMapping("/officers/{id}/approve")
    public ApiResponse approveOfficer(
            @PathVariable String id) {

        return adminService.approveOfficer(id);
    }

    @PutMapping("/officers/{id}/suspend")
    public ApiResponse suspendOfficer(
            @PathVariable String id) {

        return adminService.suspendOfficer(id);
    }

    @GetMapping("/dashboard")
    public DashboardStats getDashboardStats() {

        return adminService.getDashboardStats();
    }
    @GetMapping("/officer-performance")
    public List<OfficerPerformanceDTO>
    getOfficerPerformance() {

        return adminService
                .getOfficerPerformance();
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {

        return departmentService.getAllDepartments();
    }

    @PostMapping("/departments")
    public ApiResponse addDepartment(
            @RequestBody DepartmentRequest request) {

        return departmentService.addDepartment(
                request.getName());
    }

    @PutMapping("/departments/{id}/disable")
    public ApiResponse disableDepartment(
            @PathVariable String id) {

        return departmentService.disableDepartment(id);
    }

    @PutMapping("/departments/{id}/enable")
    public ApiResponse enableDepartment(
            @PathVariable String id) {

        return departmentService.enableDepartment(id);
    }

    @GetMapping("/officers/pending")
    public List<OfficerProfileResponse>
    getPendingOfficers() {

        return adminService.getPendingOfficerDetails();
    }
    @PutMapping("/officers/{id}/reject")
    public ApiResponse rejectOfficer(
            @PathVariable String id) {

        return adminService.rejectOfficer(id);
    }
}
