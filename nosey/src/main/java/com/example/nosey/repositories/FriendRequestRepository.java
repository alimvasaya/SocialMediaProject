package com.example.nosey.repositories;

import com.example.nosey.models.FriendRequest;
import com.example.nosey.models.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequestStatus status);

    List<FriendRequest> findByReceiverId(Long userId);

    // This method should check if a friend request exists and return a boolean
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FriendRequest f WHERE f.sender.id = :senderId AND f.receiver.id = :receiverId")
    boolean existsBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    // This method should retrieve the FriendRequest object if it exists
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}

