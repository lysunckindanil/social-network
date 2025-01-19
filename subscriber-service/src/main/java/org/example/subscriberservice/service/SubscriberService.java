package org.example.subscriberservice.service;


import lombok.RequiredArgsConstructor;
import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.dto.ProfileDto;
import org.example.subscriberservice.model.Profile;
import org.example.subscriberservice.model.ProfileSubscriber;
import org.example.subscriberservice.repo.ProfileRepository;
import org.example.subscriberservice.repo.ProfileSubscriberRepository;
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
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) return;
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) return;
        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isEmpty()) {
            ProfileSubscriber profileSubscriber = ProfileSubscriber.builder().profile(profileOptional.get()).subscriber(subscriberOptional.get()).build();
            profileSubscriberRepository.save(profileSubscriber);
        }
    }

    public void deleteSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
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
