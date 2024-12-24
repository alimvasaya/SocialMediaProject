package com.example.nosey.services;

import com.example.nosey.DTO.ReactionsDTO.ReactionRequestDTO;
import com.example.nosey.DTO.ReactionsDTO.ReactionResponseDTO;
import com.example.nosey.models.Post;
import com.example.nosey.models.Reaction;
import com.example.nosey.models.ReactionType;
import com.example.nosey.models.User;
import com.example.nosey.repositories.PostRepository;
import com.example.nosey.repositories.ReactionRepository;
import com.example.nosey.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public ReactionResponseDTO toggleReaction(String email, ReactionRequestDTO request) {
        // Fetch the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch the post
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Check if the user has already reacted
        Optional<Object> existingReaction = reactionRepository.findByPostAndUser(post, user);

        boolean isLiked;
        if (existingReaction.isPresent()) {
            // Remove reaction (unlike)
            reactionRepository.delete((Reaction) existingReaction.get());
            isLiked = false;
        } else {
            // Add new reaction (like)
            Reaction reaction = new Reaction(post, user, ReactionType.LIKE);
            reactionRepository.save(reaction);
            isLiked = true;
        }

        // Count total likes
        long likeCount = reactionRepository.countByPostAndReactionType(post, ReactionType.LIKE);

        // Return the response DTO with complete parameters
        return new ReactionResponseDTO(
                null, // No specific ID for the DTO
                post.getId(),
                user.getId(),
                likeCount,
                isLiked,
                ReactionType.LIKE // Assuming you want to specify the type as LIKE
        );
    }

    // Consolidated getReactionsByPost method (without duplication)
    public List<ReactionResponseDTO> getReactionsByPost(Long postId, String email) {
        // Fetch the post reactions
        List<Reaction> reactions = reactionRepository.findByPostId(postId);

        // Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the user has liked
        boolean isLiked = reactions.stream()
                .anyMatch(r -> r.getUser().equals(user) && r.getReactionType() == ReactionType.LIKE);

        // Map reactions to DTO
        long likeCount = reactionRepository.countByPostIdAndReactionType(postId, ReactionType.LIKE);
        return reactions.stream()
                .map(r -> new ReactionResponseDTO(
                        r.getId(),
                        r.getPost().getId(),
                        r.getUser().getId(),
                        likeCount,
                        isLiked,
                        r.getReactionType()))
                .collect(Collectors.toList());
    }

    public Long countLikesForPost(Long postId) {
        return reactionRepository.countByPostIdAndReactionType(postId, ReactionType.LIKE);
    }
}