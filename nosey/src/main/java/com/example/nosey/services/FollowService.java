package com.example.nosey.services;

import com.example.nosey.DTO.FollowDTO.FollowRequestDTO;
import com.example.nosey.DTO.FollowDTO.FollowResponseDTO;
import com.example.nosey.models.Follow;
import com.example.nosey.models.User;
import com.example.nosey.repositories.FollowRepository;
import com.example.nosey.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    // Follow a user
    public FollowResponseDTO followUser(FollowRequestDTO followRequest) {
        Long followerId = followRequest.getFollowerId();
        Long followingId = followRequest.getFollowingId();

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower user not found."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Following user not found."));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            return new FollowResponseDTO("Already following this user.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);

        return new FollowResponseDTO("Successfully followed the user.");
    }

    // Unfollow a user
    public FollowResponseDTO unfollowUser(FollowRequestDTO followRequest) {
        Long followerId = followRequest.getFollowerId();
        Long followingId = followRequest.getFollowingId();

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower user not found."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Following user not found."));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following);
        if (follow == null) {
            return new FollowResponseDTO("You are not following this user.");
        }

        followRepository.delete(follow);
        return new FollowResponseDTO("Successfully unfollowed the user.");
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower user not found."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Following user not found."));

        return followRepository.existsByFollowerAndFollowing(follower, following);
    }
}