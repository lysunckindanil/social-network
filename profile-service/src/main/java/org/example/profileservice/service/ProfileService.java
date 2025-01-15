package org.example.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.model.Profile;
import org.example.profileservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;


    public ProfileDto getProfileByUsername(String username) {
        Optional<Profile> profile_optional = profileRepository.findByUsername(username);
        if (profile_optional.isEmpty()) return null;
        Profile profile = profile_optional.get();
        return ProfileDto.builder()
                .username(profile.getUsername())
                .email(profile.getEmail())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }

    public List<ProfileDto> getAllProfiles() {
        return profileRepository.findAll().stream().map((profile ->
                ProfileDto.builder()
                        .username(profile.getUsername())
                        .email(profile.getEmail())
                        .photoUrl(profile.getPhotoUrl())
                        .build()
        )).toList();
    }
}
