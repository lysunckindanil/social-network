package org.example.profileservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.model.Profile;
import org.example.profileservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public ProfileDto getProfileByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ProfileDto.builder()
                .username(profile.getUsername())
                .email(profile.getEmail())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }

    @Transactional
    public void deleteProfile(String username) {
        profileRepository.deleteByUsername(username);
    }
}
