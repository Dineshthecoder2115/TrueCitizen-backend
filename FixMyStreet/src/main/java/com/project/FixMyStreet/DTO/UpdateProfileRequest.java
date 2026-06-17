package com.project.FixMyStreet.DTO;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String name;
    private String phoneNumber;

    private String addressLine1;

    private String city;

    private String district;

    private String state;

    private String pincode;
}