package com.example.nosey.repositories;

import com.example.nosey.models.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserId(Long userId);
    void deleteByUserIdAndFriendId(Long userId, Long friendId);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
}
