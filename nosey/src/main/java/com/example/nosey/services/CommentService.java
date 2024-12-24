package com.example.nosey.services;

import com.example.nosey.DTO.CommentDTO.CommentRequestDTO;
import com.example.nosey.DTO.CommentDTO.CommentResponseDTO;
import com.example.nosey.models.Comment;
import com.example.nosey.models.Post;
import com.example.nosey.models.User;
import com.example.nosey.models.UserProfile;
import com.example.nosey.repositories.CommentRepository;
import com.example.nosey.repositories.PostRepository;
import com.example.nosey.repositories.UserProfileRepository;
import com.example.nosey.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public CommentResponseDTO addComment(String email, CommentRequestDTO request) {
        // Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch post
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Fetch user profile to get the user's full name
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        // Create and save comment
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        // Return DTO with the user's full name
        String userName = userProfile.getFirstName() + " " + userProfile.getLastName();
        return new CommentResponseDTO(
                savedComment.getId(),
                savedComment.getPost().getId(),
                savedComment.getUser().getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUpdatedAt(),
                userName // Pass the user's full name here
        );
    }

    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(c -> {
                    UserProfile userProfile = userProfileRepository.findByUserId(c.getUser().getId())
                            .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
                    return new CommentResponseDTO(
                            c.getId(),
                            c.getPost().getId(),
                            c.getUser().getId(),
                            c.getContent(),
                            c.getCreatedAt(),
                            c.getUpdatedAt(),
                            userProfile.getFirstName() + " " + userProfile.getLastName() // Add full name
                    );
                })
                .collect(Collectors.toList());
    }
}
