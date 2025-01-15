package org.example.friends_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribed;
import org.example.friends_service.repo.ProfileSubscribedRepository;
import org.example.friends_service.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscribedService {
    private final ProfileSubscribedRepository friendsRepository;
    private final ProfileRepository profileRepository;

    public List<String> getSubscribedUsernamesByProfileUsername(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfileSubscribed> friends = friendsRepository.findProfileSubscribedByProfile(profile);
        if (friends.isPresent()) {
            return friends.get().getFriends().stream().map(Profile::getUsername).toList();
        }
        return new ArrayList<>();
    }

    // actually this service is needed for storing profiles and who subscribed on them
    public void addSubscribedByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribed> friends_optional = friendsRepository.findProfileSubscribedByProfile(friend);
        if (friends_optional.isPresent()) {
            ProfileSubscribed friends = friends_optional.get();
            friends.addFriend(profile);
            friendsRepository.save(friends);
        } else {
            ProfileSubscribed friends_new = new ProfileSubscribed();
            friends_new.setProfile(friend);
            friends_new.addFriend(profile);
            friendsRepository.save(friends_new);
        }
    }

    public void deleteSubscribedByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribed> friends_optional = friendsRepository.findProfileSubscribedByProfile(friend);
        if (friends_optional.isPresent()) {
            ProfileSubscribed friends = friends_optional.get();
            friends.deleteFriend(profile);
            friendsRepository.save(friends);
        }
    }
}
