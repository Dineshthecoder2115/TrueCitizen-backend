package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.DTO.*;
import com.project.FixMyStreet.Enums.AccountStatus;
import com.project.FixMyStreet.Enums.OfficerStatus;
import com.project.FixMyStreet.Enums.Role;
import com.project.FixMyStreet.Models.CitizenProfile;
import com.project.FixMyStreet.Models.OfficerProfile;
import com.project.FixMyStreet.Models.User;
import com.project.FixMyStreet.Repository.CitizenProfileRepository;
import com.project.FixMyStreet.Repository.OfficerProfileRepository;
import com.project.FixMyStreet.Repository.UserRepository;
import com.project.FixMyStreet.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class AuthService {
    @Autowired

    private final UserRepository userRepository;

    @Autowired
    private CitizenProfileRepository citizenProfileRepository;

    @Autowired
    private final OfficerProfileRepository officerProfileRepository;
    @Autowired
    private final OtpService otpService;
    @Autowired
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ImageUploadService imageUploadService;

    public ApiResponse registerCitizen(RegisterCitizenRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email Already Registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CITIZEN)
                .verified(false)
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser =
                userRepository.save(user);

        CitizenProfile citizen =
                CitizenProfile.builder()
                        .userId(savedUser.getId())
                        .addressLine1(
                                request.getAddressLine1())
                        .city(request.getCity())
                        .district(request.getDistrict())
                        .state(request.getState())
                        .pincode(request.getPincode())
                        .build();

        citizenProfileRepository.save(citizen);

        String otp =
                otpService.generateOtp();

        otpService.saveOtp(
                savedUser.getEmail(),
                otp);

        emailService.sendOtp(
                savedUser.getEmail(),
                otp);

        return new ApiResponse(
                true,
                "Citizen registered successfully. OTP sent.");
    }

    public ApiResponse registerOfficer(
            RegisterOfficerRequest request, MultipartFile idCard) {

        if(userRepository.existsByEmail(
                request.getEmail())) {

            return new ApiResponse(
                    false,
                    "Email already registered");
        }

        if (officerProfileRepository.existsByEmployeeId(
                request.getEmployeeId())) {

            return new ApiResponse(
                    false,
                    "Employee ID already exists");
        }
        long start = System.currentTimeMillis();

        String idCardUrl = imageUploadService.uploadImage(idCard);

        System.out.println(
                "Image Upload Time = " +
                        (System.currentTimeMillis() - start) + " ms"
        );


        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .phoneNumber(
                        request.getPhoneNumber())
                .role(Role.OFFICER)
                .verified(false)
                .accountStatus(AccountStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser =
                userRepository.save(user);

        OfficerProfile officer =
                OfficerProfile.builder()
                        .userId(savedUser.getId())
                        .employeeId(
                                request.getEmployeeId())
                        .designation(
                                request.getDesignation())
                        .department(
                                request.getDepartment())
                        .officeAddress(
                                request.getOfficeAddress())
                        .yearsOfExperience(
                                request.getYearsOfExperience())
                        .idCardUrl(
                                idCardUrl)
                        .status(
                                OfficerStatus.PENDING)
                        .totalAssignedCases(0)
                        .totalResolvedCases(0)
                        .currentActiveCases(0)
                        .build();

        officerProfileRepository.save(
                officer);

        String otp =
                otpService.generateOtp();

        otpService.saveOtp(
                savedUser.getEmail(),
                otp);
        System.out.println("OTP Save = "
                + (System.currentTimeMillis() - start));

        emailService.sendOtp(
                savedUser.getEmail(),
                otp);
        System.out.println("After Email Call = "
                + (System.currentTimeMillis() - start));

        return new ApiResponse(
                true,
                "Officer registered successfully. OTP sent.");
    }


    public ApiResponse verifyOtp(
            OtpVerificationRequest request) {

        boolean valid =
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtp());

        if(!valid) {

            return new ApiResponse(
                    false,
                    "Invalid or expired OTP");
        }

        User user =
                userRepository.findByEmail(
                                request.getEmail())
                        .orElseThrow();

        user.setVerified(true);

        userRepository.save(user);

        otpService.deleteOtp(
                request.getEmail());

        return new ApiResponse(
                true,
                "OTP verified successfully");
    }


    public LoginResponse login(LoginRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );



        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!user.isVerified())
        {
            throw new RuntimeException("Please verify your Email first");

        }
        if(user.getRole() == Role.OFFICER){

            OfficerProfile officer =
                    officerProfileRepository
                            .findByUserId(user.getId())
                            .orElseThrow();

            if(officer.getStatus()
                    == OfficerStatus.PENDING){

                throw new RuntimeException(
                        "Your account is awaiting admin approval."
                );
            }

            if(officer.getStatus()
                    == OfficerStatus.REJECTED){

                throw new RuntimeException(
                        "Your registration was rejected."
                );
            }

            if(officer.getStatus()
                    == OfficerStatus.SUSPENDED){

                throw new RuntimeException(
                        "Your account has been suspended."
                );
            }
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getId()
        );
    }

    public ApiResponse resendOtp(String email)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if(user.isVerified())
        {
            return new ApiResponse(
                    false,
                    "User already verified");
        }

        String otp = otpService.generateOtp();

        otpService.saveOtp(email, otp);

        emailService.sendOtp(email, otp);

        return new ApiResponse(
                true,
                "OTP sent successfully");
    }
}




