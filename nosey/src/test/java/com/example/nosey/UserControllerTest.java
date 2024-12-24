package com.example.nosey;
import com.example.nosey.DTO.UserDTO.*;
import com.example.nosey.controllers.UserController;
import com.example.nosey.models.User;
import com.example.nosey.repositories.UserRepository;
import com.example.nosey.services.UserService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private JwtUtil jwtUtil;
    private JwtFilter jwtFilter;
    private UserRepository userRepository;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jwtUtil = mock(JwtUtil.class);
        jwtFilter = mock(JwtFilter.class);
        userRepository = mock(UserRepository.class);
        userController = new UserController(userService, jwtUtil, jwtFilter, userRepository);
    }

    @Test
    void testGetLoggedInUser_ValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "valid-token";
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(jwtFilter.extractTokenFromCookie(request)).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponseDTO> response = userController.getLoggedInUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(email, response.getBody().getEmail());
    }

    @Test
    void testGetLoggedInUser_InvalidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "invalid-token";

        when(jwtFilter.extractTokenFromCookie(request)).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        ResponseEntity<UserResponseDTO> response = userController.getLoggedInUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testRegisterHandler_Success() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
        User user = new User();
        user.setEmail(userDTO.getEmail());

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(userService.registerUser(userDTO)).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("valid-token");

        ResponseEntity<UserResponseDTO> result = userController.registerHandler(userDTO, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(userDTO.getEmail(), result.getBody().getEmail());
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testRegisterHandler_UserAlreadyExists() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(userService.registerUser(userDTO)).thenThrow(new RuntimeException("User already exists"));

        ResponseEntity<UserResponseDTO> result = userController.registerHandler(userDTO, response);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void testLoginHandler_Success() {
        UserLoginDTO userDTO = new UserLoginDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userService.loginUser(userDTO)).thenReturn(true);
        when(jwtUtil.generateToken(userDTO.getEmail())).thenReturn("valid-token");

        ResponseEntity<UserLoginResponseDTO> result = userController.loginHandler(userDTO, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Login successful!", result.getBody().getMessage());
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testLoginHandler_InvalidCredentials() {
        UserLoginDTO userDTO = new UserLoginDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("wrongpassword");
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userService.loginUser(userDTO)).thenReturn(false);

        ResponseEntity<UserLoginResponseDTO> result = userController.loginHandler(userDTO, response);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Invalid email or password.", result.getBody().getMessage());
    }

    @Test
    void testLogoutHandler() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<String> result = userController.logoutHandler(response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Logged out successfully.", result.getBody());
        verify(response, times(1)).addCookie(any(Cookie.class));
    }
}