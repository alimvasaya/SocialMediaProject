package com.example.nosey.services;

import com.example.nosey.DTO.PasswordChangeDTO;
import com.example.nosey.DTO.UserDTO.UserLoginDTO;
import com.example.nosey.DTO.UserDTO.UserRegistrationDTO;
import com.example.nosey.exception.PasswordMismatchException;
import com.example.nosey.exception.UserAlreadyExistsException;
import com.example.nosey.exception.UserNotFoundException;
import com.example.nosey.models.User;
import com.example.nosey.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists.");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public boolean loginUser(UserLoginDTO userDTO) {
        return userRepository.findByEmail(userDTO.getEmail())
                .filter(user -> passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
                .isPresent();
    }

    public void changePassword(Long userId, PasswordChangeDTO passwordDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Old password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

//    public Optional<User> findUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }


    // Other methods
