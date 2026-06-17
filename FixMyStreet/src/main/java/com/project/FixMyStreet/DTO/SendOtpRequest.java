package com.project.FixMyStreet.DTO;


import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SendOtpRequest {

    @Email
    private String email;
}