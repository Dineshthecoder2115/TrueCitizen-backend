package com.project.FixMyStreet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficerProfileResponse {

    private String id;
    private String employeeId;
    private String designation;
    private String department;

    private String officeAddress;
    private Integer yearsOfExperience;
    private String idCardUrl;

    private Integer totalAssignedCases;
    private Integer totalResolvedCases;
    private Integer currentActiveCases;
    private String phoneNumber;
    private String name;

    private String email;
}