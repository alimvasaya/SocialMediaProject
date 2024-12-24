package com.example.nosey.DTO.FollowDTO;

public class FollowRequestDTO {
    private Long followerId; // Populated in the controller
    private Long followingId;

    // Getters and setters
    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }
}