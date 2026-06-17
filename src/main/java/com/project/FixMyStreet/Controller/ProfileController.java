package com.project.FixMyStreet.Controller;

import com.project.FixMyStreet.DTO.ProfileResponse;
import com.project.FixMyStreet.DTO.UpdateProfileRequest;
import com.project.FixMyStreet.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                profileService.getMyProfile(
                        authentication.getName()
                )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {

        profileService.updateProfile(
                authentication.getName(),
                request);

        return ResponseEntity.ok(
                "Profile updated successfully");
    }
}
