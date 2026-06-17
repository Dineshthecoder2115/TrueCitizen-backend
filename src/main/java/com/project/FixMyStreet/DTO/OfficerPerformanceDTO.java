package com.project.FixMyStreet.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfficerPerformanceDTO {

    private String officerId;

    private String employeeId;

    private String designation;

    private int totalAssignedCases;

    private int totalResolvedCases;

    private int currentActiveCases;

    private double resolutionRate;
}
