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
import org.example.subscriberservice.util.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (profileOptional.isEmpty()) throw new BadRequestException("Profile not found");
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) throw new BadRequestException("Subscriber not found");

        return profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get()).isPresent();
    }

    @Transactional(readOnly = true)
    public List<ProfileDto> findProfileSubscribedByPageable(GetSubscribersPageableDto dto) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(dto.getProfileUsername());
        if (profileOptional.isEmpty()) throw new BadRequestException("Profile not found");

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        return profileSubscriberRepository.findByProfile(profileOptional.get(), pageable).stream().map(ProfileSubscriber::getSubscriber).map(SubscriberService::wrapToDto).toList();

    }

    @Transactional(readOnly = true)
    public List<ProfileDto> findProfileSubscribedOnPageable(GetSubscribersPageableDto dto) {
        Optional<Profile> profileOptional = profileRepository.findByUsername(dto.getProfileUsername());
        if (profileOptional.isEmpty()) throw new BadRequestException("Profile not found");
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        return profileSubscriberRepository.findBySubscriber(profileOptional.get(), pageable).stream().map(ProfileSubscriber::getProfile).map(SubscriberService::wrapToDto).toList();
    }

    @Transactional
    public void addSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
        if (profile.equals(subscriber)) throw new BadRequestException("Profile and Subscriber should not be equal");
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) throw new BadRequestException("Profile not found");
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) throw new BadRequestException("Subscriber not found");

        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isEmpty()) {
            ProfileSubscriber profileSubscriber = new ProfileSubscriber(profileOptional.get(), subscriberOptional.get());
            profileSubscriberRepository.save(profileSubscriber);
        } else {
            throw new BadRequestException("Subscriber already exists");
        }
    }

    @Transactional
    public void deleteSubscriber(AddAndDeleteSubscriberDto dto) {
        String profile = dto.getProfileUsername();
        String subscriber = dto.getSubscriberUsername();
        Optional<Profile> profileOptional = profileRepository.findByUsername(profile);
        if (profileOptional.isEmpty()) throw new BadRequestException("Profile not found");
        Optional<Profile> subscriberOptional = profileRepository.findByUsername(subscriber);
        if (subscriberOptional.isEmpty()) throw new BadRequestException("Subscriber not found");

        Optional<ProfileSubscriber> profileSubscriberOptional = profileSubscriberRepository.findByProfileAndSubscriber(profileOptional.get(), subscriberOptional.get());
        if (profileSubscriberOptional.isEmpty())
            throw new BadRequestException("Profile and Subscriber have no relations");
        profileSubscriberRepository.delete(profileSubscriberOptional.get());
    }


    private static ProfileDto wrapToDto(Profile profile) {
        return ProfileDto.builder().username(profile.getUsername()).email(profile.getEmail()).photoUrl(profile.getPhotoUrl()).build();
    }
}
