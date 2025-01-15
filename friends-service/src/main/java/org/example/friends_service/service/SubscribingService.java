package org.example.friends_service.service;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribing;
import org.example.friends_service.repo.ProfileRepository;
import org.example.friends_service.repo.ProfileSubscribingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscribingService {
    private final ProfileSubscribingRepository profileSubscribingRepository;

    private final ProfileRepository profileRepository;

    public List<String> getSubscribingUsernamesByProfileUsername(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfileSubscribing> friends = profileSubscribingRepository.findProfileSubscribingByProfile(profile);
        if (friends.isPresent()) {
            return friends.get().getSubscribing().stream().map(Profile::getUsername).toList();
        }
        return new ArrayList<>();
    }

    // actually this service is needed for storing profiles and whose they subscribed to
    public void addSubscribingByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribing> friends_optional = profileSubscribingRepository.findProfileSubscribingByProfile(profile);
        if (friends_optional.isPresent()) {
            ProfileSubscribing subscribing = friends_optional.get();
            subscribing.addFriend(friend);
            profileSubscribingRepository.save(subscribing);
        } else {
            ProfileSubscribing subscribing_new = new ProfileSubscribing();
            subscribing_new.setProfile(profile);
            subscribing_new.addFriend(friend);
            profileSubscribingRepository.save(subscribing_new);
        }
    }

    public void deleteSubscribingByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribing> friends_optional = profileSubscribingRepository.findProfileSubscribingByProfile(profile);
        if (friends_optional.isPresent()) {
            ProfileSubscribing subscribing = friends_optional.get();
            subscribing.deleteFriend(friend);
            profileSubscribingRepository.save(subscribing);
        }
    }
}
