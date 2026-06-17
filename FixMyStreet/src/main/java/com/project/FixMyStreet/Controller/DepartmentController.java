package com.project.FixMyStreet.Controller;

import com.project.FixMyStreet.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // Citizen/Officer use this
    @GetMapping
    public List<String> getActiveDepartments() {

        return departmentService.getActiveDepartments();
    }
}
