package com.example.nosey;

import com.example.nosey.DTO.UserDTO.UserProfileDTO;
import com.example.nosey.controllers.UserProfileController;
import com.example.nosey.services.UserProfileService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileControllerTest {

    private UserProfileService userProfileService;
    private JwtUtil jwtUtil;
    private JwtFilter jwtFilter;
    private UserProfileController userProfileController;

    @BeforeEach
    void setUp() {
        userProfileService = mock(UserProfileService.class);
        jwtUtil = mock(JwtUtil.class);
        jwtFilter = mock(JwtFilter.class);
        userProfileController = new UserProfileController(userProfileService, jwtUtil, jwtFilter, jwtUtil);
    }

    @Test
    void testGetProfile_ValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "valid-token";
        String email = "test@example.com";
        UserProfileDTO profileDTO = new UserProfileDTO("John", "Doe", "1234567890", "123 Street", 30, "profile.jpg");

        when(jwtFilter.extractTokenFromCookie(request)).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenReturn(email);
        when(userProfileService.getUserProfileByEmail(email)).thenReturn(profileDTO);

        ResponseEntity<UserProfileDTO> response = userProfileController.getProfile(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDTO, response.getBody());
    }

    @Test
    void testUpdateProfile_ValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "valid-token";
        String email = "test@example.com";
        UserProfileDTO profileDTO = new UserProfileDTO("John", "Doe", "1234567890", "123 Street", 30, "profile.jpg");

        when(jwtFilter.extractTokenFromCookie(request)).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenReturn(email);

        ResponseEntity<String> response = userProfileController.updateProfile(request, profileDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile updated successfully.", response.getBody());
    }

    @Test
    void testDeleteProfile() {
        String email = "test@example.com";

        ResponseEntity<String> response = userProfileController.deleteProfile(email);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile deleted successfully.", response.getBody());
    }

    @Test
    void testGetUserById() {
        Long id = 1L;
        UserProfileDTO profileDTO = new UserProfileDTO("John", "Doe", "1234567890", "123 Street", 30, "profile.jpg");

        when(userProfileService.getUserProfileById(id)).thenReturn(profileDTO);

        ResponseEntity<UserProfileDTO> response = userProfileController.getUserById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDTO, response.getBody());
    }
}