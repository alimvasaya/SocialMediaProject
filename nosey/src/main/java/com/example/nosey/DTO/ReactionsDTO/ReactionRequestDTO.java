package com.example.nosey.DTO.ReactionsDTO;

import com.example.nosey.models.ReactionType;

public class ReactionRequestDTO {
    private Long postId;
    private Long userId;
    private ReactionType reactionType;

    public ReactionRequestDTO(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}