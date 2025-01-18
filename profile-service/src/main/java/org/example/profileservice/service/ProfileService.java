package org.example.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.GetProfilesPageableDto;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.model.Profile;
import org.example.profileservice.repo.ProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;


    public ProfileDto getProfileByUsername(String username) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(username);
        if (profileOptional.isEmpty()) return null;
        return wrapToDto(profileOptional.get());
    }

    public List<ProfileDto> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(ProfileService::wrapToDto)
                .toList();
    }

    public List<ProfileDto> getAllProfilesPageable(GetProfilesPageableDto dto) {
        int page = dto.getPage();
        int size = dto.getSize();
        return profileRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(ProfileService::wrapToDto)
                .toList();
    }

    private static ProfileDto wrapToDto(Profile profile) {
        return ProfileDto.builder()
                .username(profile.getUsername())
                .email(profile.getEmail())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }
}
