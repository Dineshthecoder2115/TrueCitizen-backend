package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.DTO.ApiResponse;
import com.project.FixMyStreet.Models.Department;
import com.project.FixMyStreet.Repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<String> getActiveDepartments() {
        return departmentRepository.findAll()
                .stream()
                .filter(Department::isActive)
                .map(Department::getName)
                .toList();
    }

    public ApiResponse addDepartment(String name) {

        if (departmentRepository.existsByNameIgnoreCase(name)) {
            return new ApiResponse(false,
                    "Department already exists");
        }

        Department department = Department.builder()
                .name(name)
                .active(true)
                .build();

        departmentRepository.save(department);

        return new ApiResponse(
                true,
                "Department added successfully");
    }

    public ApiResponse disableDepartment(String id) {

        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));

        department.setActive(false);

        departmentRepository.save(department);

        return new ApiResponse(
                true,
                "Department disabled");
    }

    public ApiResponse enableDepartment(String id) {

        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));

        department.setActive(true);

        departmentRepository.save(department);

        return new ApiResponse(
                true,
                "Department enabled");
    }
}
