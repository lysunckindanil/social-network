package org.example.friends_service.service;


import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddAndDeleteSubscriberDto;
import org.example.friends_service.dto.ProfileDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscriber;
import org.example.friends_service.repo.ProfileRepository;
import org.example.friends_service.repo.ProfileSubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriberService {
    private final ProfileSubscriberRepository profileSubscriberRepository;
    private final ProfileRepository profileRepository;

    public List<ProfileDto> getSubscribers(String username) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(username);
        if (profileOptional.isEmpty()) return new ArrayList<>();
        return profileSubscriberRepository.findAllByProfile(profileOptional.get()).stream()
                .map(ProfileSubscriber::getSubscriber)
                .map(SubscriberService::wrapToDto)
                .toList();
    }

    public List<ProfileDto> getProfileSubscribedOn(String username) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(username);
        if (profileOptional.isEmpty()) return new ArrayList<>();

        return profileSubscriberRepository.findAllBySubscriber(profileOptional.get()).stream()
                .map(ProfileSubscriber::getProfile)
                .map(SubscriberService::wrapToDto)
                .toList();
    }

    public void addSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfile_username();
        String subscriber = dto.getSubscriber_username();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) return;
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) return;
        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isEmpty()) {
            ProfileSubscriber profileSubscriber = new ProfileSubscriber();
            profileSubscriber.setProfile(profileOptional.get());
            profileSubscriber.setSubscriber(subscriberOptional.get());
            profileSubscriberRepository.save(profileSubscriber);
        }
    }

    public void deleteSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfile_username();
        String subscriber = dto.getSubscriber_username();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) return;
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) return;

        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isPresent())
            profileSubscriberRepository.delete(profileSubscriberOptional.get());
    }

    private static ProfileDto wrapToDto(Profile profile) {
        return ProfileDto.builder()
                .username(profile.getUsername())
                .email(profile.getEmail())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }
}
