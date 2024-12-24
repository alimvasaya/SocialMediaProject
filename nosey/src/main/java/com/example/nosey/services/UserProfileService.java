package com.example.nosey.services;

import com.example.nosey.DTO.UserDTO.UserProfileDTO;
import com.example.nosey.exception.UserNotFoundException;
import com.example.nosey.models.User;
import com.example.nosey.models.UserProfile;
import com.example.nosey.repositories.UserProfileRepository;
import com.example.nosey.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository, UserProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    // Update user profile using email
    public void updateUserProfileByEmail(String email, UserProfileDTO dto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setAddress(dto.getAddress());
        profile.setAge(dto.getAge());
        profile.setProfileImage(dto.getProfileImage());
        profileRepository.save(profile);
    }

    // Get user profile by email
    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        UserProfile profile = user.getProfile();
        return new UserProfileDTO(
                profile.getFirstName(), profile.getLastName(), profile.getPhoneNumber(),
                profile.getAddress(), profile.getAge(), profile.getProfileImage()
        );
    }

    // Delete user profile by email
    public void deleteUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        profileRepository.delete(user.getProfile());
    }
    public UserProfileDTO getUserProfileById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        UserProfile profile = user.getProfile();
        return new UserProfileDTO(
                profile.getFirstName(), profile.getLastName(), profile.getPhoneNumber(),
                profile.getAddress(), profile.getAge(), profile.getProfileImage()
        );
    }

}