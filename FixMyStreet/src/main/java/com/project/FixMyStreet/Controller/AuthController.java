package com.project.FixMyStreet.Controller;

import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Security.JwtUtil;
import com.project.FixMyStreet.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register/citizen")
    public ApiResponse registerCitizen(
            @Valid @RequestBody RegisterCitizenRequest request) {

        return authService.registerCitizen(request);
    }

    @PostMapping("/register/officer")
    public ApiResponse registerOfficer(
            @ModelAttribute RegisterOfficerRequest request,
            @RequestParam("idCard") MultipartFile idCard) {

        return authService.registerOfficer(request, idCard);
    }

    @PostMapping("/verify-otp")
    public ApiResponse verifyOtp(
            @Valid @RequestBody OtpVerificationRequest request) {

        return authService.verifyOtp(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request)
    {
        return authService.login(request);
    }

    @PostMapping("/resend-otp")
    public ApiResponse resendOtp(
            @RequestParam String email)
    {
        return authService.resendOtp(email);
    }

}
