package com.example.nosey.controllers;

import com.example.nosey.DTO.FriendsDTO.FriendDTO;
import com.example.nosey.DTO.FriendsDTO.FriendRequestDTO;
import com.example.nosey.models.User;
import com.example.nosey.repositories.FriendRequestRepository;
import com.example.nosey.repositories.UserRepository;
import com.example.nosey.services.FriendService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "http://localhost:3000")
class FriendController {

    private final FriendService friendService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendController(FriendService friendService, JwtUtil jwtUtil, JwtFilter jwtFilter, UserRepository userRepository, FriendRequestRepository friendRequestRepository) {
        this.friendService = friendService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(HttpServletRequest request, @RequestBody FriendRequestDTO requestDTO) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract email from JWT and find the user ID
        String email = jwtUtil.extractEmail(token);
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        // Ensure receiver ID is not null
        if (requestDTO.getSenderId() == null) {
            throw new IllegalArgumentException("Receiver ID must not be null.");
        }

        // Check if a friend request already exists
        if (friendRequestRepository.existsBySenderIdAndReceiverId(sender.getId(), requestDTO.getSenderId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A friend request already exists between these users.");
        }

        // Proceed with sending the friend request
        friendService.sendFriendRequest(sender.getId(), requestDTO.getSenderId());
        return ResponseEntity.ok("Friend request sent.");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(HttpServletRequest request, @RequestBody FriendRequestDTO requestDTO) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User receiver = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        friendService.acceptFriendRequest(receiver.getId(), requestDTO.getSenderId());
        return ResponseEntity.ok("Friend request accepted.");
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineFriendRequest(HttpServletRequest request, @RequestBody FriendRequestDTO requestDTO) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User receiver = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        friendService.declineFriendRequest(receiver.getId(), requestDTO.getSenderId());
        return ResponseEntity.ok("Friend request declined.");
    }

    @PostMapping("/unfriend")
    public ResponseEntity<String> unfriendUser(HttpServletRequest request, @RequestBody FriendDTO friendDTO) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        friendService.unfriend(user.getId(), friendDTO.getFriendId());
        return ResponseEntity.ok("Unfriended successfully.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<FriendDTO>> listFriends(HttpServletRequest request) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        List<FriendDTO> friends = friendService.getFriends(user.getId());
        return ResponseEntity.ok(friends);
    }
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getFriendStatus(HttpServletRequest request, @RequestParam Long friendId) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        boolean isFriend = friendService.isFriend(user.getId(), friendId);
        return ResponseEntity.ok(Collections.singletonMap("isFriend", isFriend));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests(HttpServletRequest request) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        List<FriendRequestDTO> friendRequests = friendService.getFriendRequests(user.getId());
        return ResponseEntity.ok(friendRequests);
    }
}