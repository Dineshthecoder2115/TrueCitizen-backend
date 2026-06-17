package com.project.FixMyStreet.Service;

import com.project.FixMyStreet.DTO.ProfileResponse;
import com.project.FixMyStreet.DTO.UpdateProfileRequest;
import com.project.FixMyStreet.Models.CitizenProfile;
import com.project.FixMyStreet.Models.User;
import com.project.FixMyStreet.Repository.CitizenProfileRepository;
import com.project.FixMyStreet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CitizenProfileRepository citizenProfileRepository;
    public ProfileResponse getMyProfile(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        CitizenProfile profile =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElse(null);

        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .addressLine1(
                        profile != null ?
                                profile.getAddressLine1() : null)
                .city(
                        profile != null ?
                                profile.getCity() : null)
                .district(
                        profile != null ?
                                profile.getDistrict() : null)
                .state(
                        profile != null ?
                                profile.getState() : null)
                .pincode(
                        profile != null ?
                                profile.getPincode() : null)
                .build();
    }

    public void updateProfile(
            String email,
            UpdateProfileRequest request) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);

        CitizenProfile profile =
                citizenProfileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found"));

        profile.setAddressLine1(
                request.getAddressLine1());

        profile.setCity(
                request.getCity());

        profile.setDistrict(
                request.getDistrict());

        profile.setState(
                request.getState());

        profile.setPincode(
                request.getPincode());

        citizenProfileRepository.save(profile);
    }
}