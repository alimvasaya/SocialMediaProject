package com.example.nosey.repositories;

import com.example.nosey.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    Optional<User> findUserByEmailAndPassword(String email, String password);

}
