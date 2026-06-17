package com.project.FixMyStreet.DTO;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterCitizenRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String addressLine1;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;
}
