package com.project.FixMyStreet.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterOfficerRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String employeeId;

    @NotBlank
    private String designation;


    private String  department;

    @NotBlank
    private String officeAddress;

    private Integer yearsOfExperience;

    private String idCardUrl;
}
