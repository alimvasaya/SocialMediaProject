package com.example.nosey.controllers;

import com.example.nosey.DTO.UserDTO.UserProfileDTO;
import com.example.nosey.services.UserProfileService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final JwtFilter jwtfilter;
    private final JwtUtil jwtUtil;
    public UserProfileController(UserProfileService userProfileService, JwtUtil jwtUtil, JwtFilter jwtfilter, JwtUtil jwtUtil1) {
        this.userProfileService = userProfileService;

        this.jwtfilter = jwtfilter;
        this.jwtUtil = jwtUtil1;
    }

    // Get profile by email
    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileDTO> getProfile(HttpServletRequest request) {
        String token = jwtfilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwtUtil.extractEmail(token);
        UserProfileDTO profileDTO = userProfileService.getUserProfileByEmail(email);
        return ResponseEntity.ok(profileDTO);
    }


    @PutMapping("/user/profile")
    public ResponseEntity<String> updateProfile(HttpServletRequest request, @RequestBody UserProfileDTO profileDTO) {
        String token = jwtfilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwtUtil.extractEmail(token);
        userProfileService.updateUserProfileByEmail(email, profileDTO);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    // Delete profile by email
    @DeleteMapping("/profile/{email}")
    public ResponseEntity<String> deleteProfile(@PathVariable String email) {
        userProfileService.deleteUserProfileByEmail(email);
        return ResponseEntity.ok("Profile deleted successfully.");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable Long id) {
        UserProfileDTO profileDTO = userProfileService.getUserProfileById(id);
        return ResponseEntity.ok(profileDTO);
    }
}