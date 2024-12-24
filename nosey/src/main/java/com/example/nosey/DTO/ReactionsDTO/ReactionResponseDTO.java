package com.example.nosey.DTO.ReactionsDTO;

import com.example.nosey.models.ReactionType;

public class ReactionResponseDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private long likeCount;
    private boolean isLiked;
    private ReactionType reactionType;

    public ReactionResponseDTO(Long id, Long postId, Long userId, long likeCount, boolean isLiked, ReactionType reactionType) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.reactionType = reactionType;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }
}