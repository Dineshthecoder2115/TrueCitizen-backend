package com.project.FixMyStreet.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerificationRequest {

    @Email
    private String email;

    @NotBlank
    private String otp;
}
