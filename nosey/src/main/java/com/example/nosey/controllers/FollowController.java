package com.example.nosey.controllers;

import com.example.nosey.DTO.FollowDTO.FollowRequestDTO;
import com.example.nosey.DTO.FollowDTO.FollowResponseDTO;
import com.example.nosey.models.User;
import com.example.nosey.repositories.UserRepository;
import com.example.nosey.services.FollowService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin(origins = "http://localhost:3000")
public class FollowController {

    private final FollowService followService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;

    @Autowired
    public FollowController(FollowService followService, JwtUtil jwtUtil, JwtFilter jwtFilter, UserRepository userRepository) {
        this.followService = followService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowResponseDTO> followUser(HttpServletRequest request, @RequestBody FollowRequestDTO followRequest) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract email from JWT
        String email = jwtUtil.extractEmail(token);

        // Find the logged-in user (follower) by email
        User follower = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        // Set the followerId in the request DTO
        followRequest.setFollowerId(follower.getId());

        // Call the service
        FollowResponseDTO response = followService.followUser(followRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<FollowResponseDTO> unfollowUser(HttpServletRequest request, @RequestBody FollowRequestDTO followRequest) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        followRequest.setFollowerId(follower.getId());

        FollowResponseDTO response = followService.unfollowUser(followRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getFollowStatus(HttpServletRequest request, @RequestParam Long followingId) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        boolean isFollowing = followService.isFollowing(follower.getId(), followingId);
        return ResponseEntity.ok(Collections.singletonMap("isFollowing", isFollowing));
    }
}