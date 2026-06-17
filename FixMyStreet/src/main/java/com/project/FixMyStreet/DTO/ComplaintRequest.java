package com.project.FixMyStreet.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ComplaintRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;


    private String  department;

    private MultipartFile file;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String pincode;

    private Double latitude;

    private Double longitude;
}