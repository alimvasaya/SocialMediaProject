package com.example.nosey.repositories;

import com.example.nosey.models.Follow;
import com.example.nosey.models.User;
import com.example.nosey.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);

    List<Follow> findAllByFollower(User follower);


    Follow findByFollowerAndFollowing(User follower, User following);
}