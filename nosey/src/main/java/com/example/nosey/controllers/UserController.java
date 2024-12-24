package com.example.nosey.controllers;


import com.example.nosey.DTO.PasswordChangeDTO;
import com.example.nosey.DTO.UserDTO.*;
import com.example.nosey.exception.UserAlreadyExistsException;
import com.example.nosey.models.User;
import com.example.nosey.repositories.UserRepository;
import com.example.nosey.services.UserService;
import com.example.nosey.utils.JwtFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.nosey.utils.JwtUtil;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;
    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, JwtFilter jwtFilter, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
    }
    @GetMapping("/loggedInUser")
    public ResponseEntity<UserResponseDTO> getLoggedInUser(HttpServletRequest request) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract email from JWT token
        String email = jwtUtil.extractEmail(token);

        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        // Return user details in response DTO
        return ResponseEntity.ok(new UserResponseDTO(user, null));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerHandler(@RequestBody UserRegistrationDTO userDTO, HttpServletResponse response) {
        try {
            User user = userService.registerUser(userDTO);
            String token = jwtUtil.generateToken(user.getEmail());

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Set to true in production (requires HTTPS)
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(new UserResponseDTO(user, null)); // Token no longer returned in the body
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginHandler(@RequestBody UserLoginDTO userDTO, HttpServletResponse response) {
        try {
            boolean isAuthenticated = userService.loginUser(userDTO);
            if (isAuthenticated) {
                String token = jwtUtil.generateToken(userDTO.getEmail());

                Cookie jwtCookie = new Cookie("jwt", token);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(false); // Set to true in production
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                response.addCookie(jwtCookie);

                return ResponseEntity.ok(new UserLoginResponseDTO("Login successful!", null));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponseDTO("Invalid email or password.", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserLoginResponseDTO("An error occurred during login.", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutHandler(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Set to true in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Remove the cookie
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Logged out successfully.");
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody PasswordChangeDTO passwordDTO) {
        userService.changePassword(userId, passwordDTO);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @GetMapping("/auth/check")
    public ResponseEntity<Void> checkAuth(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

}