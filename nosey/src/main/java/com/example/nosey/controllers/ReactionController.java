package com.example.nosey.controllers;

import com.example.nosey.DTO.ReactionsDTO.ReactionRequestDTO;
import com.example.nosey.DTO.ReactionsDTO.ReactionResponseDTO;
import com.example.nosey.services.ReactionService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reactions")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    private final JwtFilter jwtfilter;
    private final JwtUtil jwtUtil;

    public ReactionController(ReactionService reactionService, JwtFilter jwtfilter, JwtUtil jwtUtil) {
        this.reactionService = reactionService;
        this.jwtfilter = jwtfilter;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<ReactionResponseDTO> toggleReaction(HttpServletRequest request, @RequestBody ReactionRequestDTO requestDTO) {
        String token = jwtfilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract email from the token
        String email = jwtUtil.extractEmail(token);

        // Call the service with the email
        ReactionResponseDTO response = reactionService.toggleReaction(email, requestDTO);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> getReactionsAndLikes(
            @PathVariable Long postId,
            HttpServletRequest request) {
        String token = jwtfilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract email from token
        String email = jwtUtil.extractEmail(token);

        // Fetch reactions using email
        List<ReactionResponseDTO> reactions = reactionService.getReactionsByPost(postId, email);
        Long likeCount = reactionService.countLikesForPost(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("reactions", reactions);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }
}