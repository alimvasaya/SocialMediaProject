package com.example.nosey.repositories;

import com.example.nosey.models.Post;
import com.example.nosey.models.Reaction;
import com.example.nosey.models.ReactionType;
import com.example.nosey.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPostId(Long postId);
    boolean existsByPostAndUser(Post post, User user);

    Long countByPostIdAndReactionType(Long postId, ReactionType like);

    Optional<Object> findByPostAndUser(Post post, User user);

    long countByPostAndReactionType(Post post, ReactionType like);
}