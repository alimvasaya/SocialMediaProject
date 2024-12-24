package com.example.nosey.controllers;

import com.example.nosey.DTO.PostDTO.PostRequestDTO;
import com.example.nosey.DTO.PostDTO.PostResponseDTO;
import com.example.nosey.models.Post;
import com.example.nosey.repositories.PostRepository;
import com.example.nosey.repositories.UserRepository;
import com.example.nosey.models.User;
import com.example.nosey.services.PostService;
import com.example.nosey.utils.JwtFilter;
import com.example.nosey.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PostRepository postRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, UserRepository userRepository, JwtFilter jwtFilter, JwtUtil jwtUtil) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.jwtFilter = jwtFilter;
        this.jwtUtil = jwtUtil;
    }

    // Create a new post
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "media", required = false) MultipartFile media,
            HttpServletRequest request
    ) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostRequestDTO postRequest = new PostRequestDTO(user.getId(), title, content, media);

        try {
            PostResponseDTO response = postService.createPost(postRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get all posts
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDTO>> getFeed(HttpServletRequest request) {
        String token = jwtFilter.extractTokenFromCookie(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        List<PostResponseDTO> feedPosts = postService.getFeedPosts(email);
        return ResponseEntity.ok(feedPosts);
    }
    // Get posts by user
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        try {
            Long id = postId;
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            return ResponseEntity.ok(postService.mapToPostResponseDTO(post));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


//
//    // Delete a post
//    @DeleteMapping("/{postId}")
//    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
//        String message = postService.deletePost(postId);
//        return ResponseEntity.ok(message);
//    }
