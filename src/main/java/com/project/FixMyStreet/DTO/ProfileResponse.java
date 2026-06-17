package com.project.FixMyStreet.DTO;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {

    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;

    private String addressLine1;
    private String city;
    private String district;
    private String state;
    private String pincode;
}