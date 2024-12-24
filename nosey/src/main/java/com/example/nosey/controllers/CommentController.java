package com.example.nosey.controllers;

import com.example.nosey.DTO.CommentDTO.CommentRequestDTO;
import com.example.nosey.DTO.CommentDTO.CommentResponseDTO;
import com.example.nosey.services.CommentService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private final JwtFilter jwtfilter;
    private final JwtUtil jwtUtil;

    public CommentController(CommentService commentService, JwtFilter jwtfilter, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtfilter = jwtfilter;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(HttpServletRequest request, @RequestBody CommentRequestDTO commentRequest) {
        String token = jwtfilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        CommentResponseDTO response = commentService.addComment(email, commentRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments); // This should work as long as 'comments' is a List<CommentResponseDTO>
    }
}
