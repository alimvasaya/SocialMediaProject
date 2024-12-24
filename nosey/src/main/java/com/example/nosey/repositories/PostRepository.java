package com.example.nosey.repositories;
import com.example.nosey.models.Post;
import com.example.nosey.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

    List<Post> findByUserIn(List<User> users);
    List<Post> findByTitleContainingIgnoreCase(String title);


    List<Post> findByUserId(Long userId);
}