package com.example.nosey.repositories;

import com.example.nosey.models.User;
import com.example.nosey.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser(User user);

    Optional<UserProfile> findByUserId(Long userId);

    List<UserProfile> findByFirstNameContainingIgnoreCase(String firstName);

    List<UserProfile> findByLastNameContainingIgnoreCase(String lastName);

    // Optional: Search full name (combining first and last names)
    @Query("SELECT u FROM UserProfile u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<UserProfile> findByFullNameContainingIgnoreCase(@Param("query") String query);

}