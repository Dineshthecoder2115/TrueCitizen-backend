package com.project.FixMyStreet.Models;

import com.project.FixMyStreet.Enums.OfficerStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "officer_profiles")
public class OfficerProfile {

    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private String employeeId;

    private String name;

    private String designation;

    @Indexed
    private String  department;

    private String officeAddress;

    private Integer yearsOfExperience;

    private String idCardUrl;

    private OfficerStatus status;

    // Analytics
    private Integer totalAssignedCases;

    private Integer totalResolvedCases;

    private Integer currentActiveCases;
}
