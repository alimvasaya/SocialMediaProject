package com.example.nosey.services;

import com.example.nosey.DTO.FriendsDTO.FriendDTO;
import com.example.nosey.DTO.FriendsDTO.FriendRequestDTO;
import com.example.nosey.models.*;
import com.example.nosey.repositories.FriendRepository;
import com.example.nosey.repositories.FriendRequestRepository;
import com.example.nosey.repositories.UserProfileRepository;
import com.example.nosey.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    // Send a friend request from the logged-in user to the receiver
    public void sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Sender ID and Receiver ID must not be null.");
        }
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("User not found."));

        // Check if the friend request already exists
        if (friendRequestRepository.existsBySenderIdAndReceiverId(sender.getId(), receiver.getId())) {
            throw new IllegalStateException("A friend request already exists between these users.");
        }

        // If not, proceed with creating a new friend request
        FriendRequest friendRequest = new FriendRequest(sender, receiver, FriendRequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);
    }

    // Accept a friend request by the receiver
    public void acceptFriendRequest(Long receiverId, Long requestId) {
        if (receiverId == null || requestId == null) {
            throw new IllegalArgumentException("User ID and Friend ID must not be null");
        }
        Optional<User> user = userRepository.findById(receiverId);
        Optional<User> friend = userRepository.findById(requestId);
        if (user.isEmpty() || friend.isEmpty()) {
            throw new EntityNotFoundException("User or Friend not found");
        }
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found."));

        // Ensure that the receiver matches the logged-in user
        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Unauthorized.");
        }

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        // Create the friendship relation
        friendRepository.save(new Friend(request.getSender(), request.getReceiver()));
        friendRepository.save(new Friend(request.getReceiver(), request.getSender()));
    }

    // Decline a friend request by the receiver
    public void declineFriendRequest(Long receiverId, Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found."));

        // Ensure that the receiver matches the logged-in user
        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Unauthorized.");
        }

        request.setStatus(FriendRequestStatus.DECLINED);
        friendRequestRepository.save(request);
    }

    // Unfriend a user
    @Transactional
    public void unfriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new RuntimeException("Friend not found."));

        friendRepository.deleteByUserIdAndFriendId(user.getId(), friend.getId());
        friendRepository.deleteByUserIdAndFriendId(friend.getId(), user.getId());
    }

    // Get a list of friends for the logged-in user
    public List<FriendDTO> getFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        return friendRepository.findByUserId(user.getId()).stream()
                .map(friend -> new FriendDTO(friend.getFriend().getId(), friend.getFriend().getId()))
                .collect(Collectors.toList());
    }

    public boolean isFriend(Long userId, Long friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }
    public List<FriendRequestDTO> getFriendRequests(Long userId) {
        List<FriendRequest> requests = friendRequestRepository.findByReceiverId(userId)
                .stream()
                .filter(request -> request.getStatus() == FriendRequestStatus.PENDING) // Include only pending requests
                .collect(Collectors.toList());

        return requests.stream()
                .map(request -> {
                    UserProfile senderProfile = userProfileRepository.findByUser(request.getSender());
                    String senderName = senderProfile != null
                            ? senderProfile.getFirstName() + " " + senderProfile.getLastName()
                            : "Unknown User";

                    return new FriendRequestDTO(
                            request.getId(),
                            request.getSender().getId(),
                            senderName,
                            FriendRequestStatus.PENDING
                    );
                })
                .collect(Collectors.toList());
    }
}

