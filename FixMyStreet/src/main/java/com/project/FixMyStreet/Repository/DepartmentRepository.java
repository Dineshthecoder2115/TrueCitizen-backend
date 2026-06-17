package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Models.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository
        extends MongoRepository<Department, String> {

    Optional<Department> findByNameAndActiveTrue(String name);
    boolean existsByNameAndActiveTrue(String name);

    boolean existsByNameIgnoreCase(String name);

}