package com.project.FixMyStreet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateOfficerProfileRequest {

    private String phoneNumber;

    private String officeAddress;

    private Integer yearsOfExperience;
    private String idCardUrl;
}