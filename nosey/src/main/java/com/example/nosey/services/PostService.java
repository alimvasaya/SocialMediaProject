package com.example.nosey.services;

import com.example.nosey.DTO.PostDTO.PostRequestDTO;
import com.example.nosey.DTO.PostDTO.PostResponseDTO;
import com.example.nosey.models.Follow;
import com.example.nosey.models.UserProfile;
import com.example.nosey.repositories.*;
import com.example.nosey.models.Post;
import com.example.nosey.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private  FriendRepository friendRepository;

    public PostResponseDTO createPost(PostRequestDTO requestDTO) {
        if (requestDTO.getUserId() == null) {
            throw new RuntimeException("User ID is missing in the request");
        }

        User author = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Post post = new Post();
        post.setUser(author);
        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());

        // Handle media (if present)
        if (requestDTO.getMedia() != null && !requestDTO.getMedia().isEmpty()) {
            try {
                String mediaUrl = mediaService.uploadMedia(requestDTO.getMedia());
                post.setMediaUrl(mediaUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload media", e);
            }
        }

        postRepository.save(post);
        return mapToPostResponseDTO(post);
    }

    public PostResponseDTO mapToPostResponseDTO(Post post) {
        UserProfile userProfile = userProfileRepository.findByUser(post.getUser());
        return new PostResponseDTO(
                post.getId(),
                post.getUser().getId(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                post.getTitle(),
                post.getContent(),
                post.getMediaUrl(),
                post.getCreatedAt()
        );
    }
    public List<PostResponseDTO> getFeedPosts(String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get users followed by the current user
        List<User> followedUsers = followRepository.findAllByFollower(currentUser)
                .stream()
                .map(Follow::getFollowing).toList();

        // Get friends of the current user
        List<User> friends = friendRepository.findByUserId(currentUser.getId())
                .stream()
                .map(friendship -> friendship.getUser().equals(currentUser) ? friendship.getFriend() : friendship.getUser()).toList();

        // Combine followed users and friends
        Set<User> feedUsers = new HashSet<>(followedUsers);
        feedUsers.addAll(friends);
        feedUsers.add(currentUser); // Include the current user's posts

        // Convert the Set to a List
        List<User> feedUserList = new ArrayList<>(feedUsers);

        // Fetch posts from the combined list of users
        return postRepository.findByUserIn(feedUserList)
                .stream()
                .map(this::mapToPostResponseDTO)
                .collect(Collectors.toList());
    }

//        // Get posts by user
//    public List<PostResponseDTO> getPostsByUser(Long userId) {
//        List<Post> posts = postRepository.findByUserId(userId);
//
//        return posts.stream()
//                .map(post -> new PostResponseDTO(post.getId(), post.getUser().getId(),
//                        post.getContent(), post.getCreatedAt()))
//                .collect(Collectors.toList());
//    }
}
//

//
//    // Delete a post
//    public String deletePost(Long postId) {
//        Optional<Post> postOptional = postRepository.findById(postId);
//
//        if (postOptional.isPresent()) {
//            postRepository.deleteById(postId);
//            return "Post deleted successfully.";
//        } else {
//            throw new RuntimeException("Post not found.");
//        }
//    }