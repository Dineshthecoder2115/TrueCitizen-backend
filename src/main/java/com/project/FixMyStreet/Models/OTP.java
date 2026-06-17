package com.project.FixMyStreet.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "otps")
public class OTP {

    @Id
    private String id;

    private String email;

    private String otp;

    private LocalDateTime expiresAt;
}
