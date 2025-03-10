package org.example.subscriberservice.service;


import lombok.RequiredArgsConstructor;
import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.dto.GetSubscribersPageableDto;
import org.example.subscriberservice.dto.IsSubscriberDto;
import org.example.subscriberservice.dto.ProfileDto;
import org.example.subscriberservice.model.Profile;
import org.example.subscriberservice.model.ProfileSubscriber;
import org.example.subscriberservice.repo.ProfileRepository;
import org.example.subscriberservice.repo.ProfileSubscriberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriberService {
    private final ProfileSubscriberRepository profileSubscriberRepository;
    private final ProfileRepository profileRepository;


    @Transactional(readOnly = true)
    public boolean isProfileSubscribedOn(IsSubscriberDto dto) {
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) return false;
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) return false;

        return profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get()).isPresent();
    }

    @Transactional(readOnly = true)
    public List<ProfileDto> findProfileSubscribedBy(String username) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(username);
        if (profileOptional.isEmpty()) return new ArrayList<>();

        return profileOptional.get().getSubscribers().stream().map(ProfileSubscriber::getSubscriber).map(SubscriberService::wrapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ProfileDto> getProfileSubscribedOn(String username) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(username);
        if (profileOptional.isEmpty()) return new ArrayList<>();

        return profileOptional.get().getSubscribing().stream().map(ProfileSubscriber::getProfile).map(SubscriberService::wrapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ProfileDto> findProfileSubscribedByPageable(GetSubscribersPageableDto dto) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(dto.getProfileUsername());
        if (profileOptional.isEmpty()) return new ArrayList<>();
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        return profileSubscriberRepository.findByProfile(profileOptional.get(), pageable)
                .stream()
                .map(ProfileSubscriber::getSubscriber)
                .map(SubscriberService::wrapToDto)
                .toList();

    }

    @Transactional(readOnly = true)
    public List<ProfileDto> getProfileSubscribedOnPageable(GetSubscribersPageableDto dto) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(dto.getProfileUsername());
        if (profileOptional.isEmpty()) return new ArrayList<>();
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        return profileSubscriberRepository.findBySubscriber(profileOptional.get(), pageable)
                .stream()
                .map(ProfileSubscriber::getProfile)
                .map(SubscriberService::wrapToDto)
                .toList();
    }

    @Transactional
    public void addSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) return;
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) return;
        if (profileOptional.equals(subscriberOptional)) return;
        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isEmpty()) {
            ProfileSubscriber profileSubscriber = new ProfileSubscriber(profileOptional.get(), subscriberOptional.get());
            profileSubscriberRepository.save(profileSubscriber);
        }
    }

    @Transactional
    public void deleteSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) return;
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) return;

        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isPresent()) profileSubscriberRepository.delete(profileSubscriberOptional.get());
    }


    private static ProfileDto wrapToDto(Profile profile) {
        return ProfileDto.builder()
                .username(profile.getUsername())
                .email(profile.getEmail())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }
}
