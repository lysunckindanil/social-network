package org.example.friends_service.service;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileFriend;
import org.example.friends_service.repo.ProfileFriendRepository;
import org.example.friends_service.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FriendsService {
    private final ProfileFriendRepository friendsRepository;
    private final ProfileRepository profileRepository;

    public List<String> getFriendsUsernamesByProfileUsername(String username) {
        Optional<Profile> optionalProfile = profileRepository.findByUsername(username);
        if (optionalProfile.isPresent()) {
            List<ProfileFriend> friends = friendsRepository.findProfileFriendsByProfile(optionalProfile.get());
            return friends.stream().map(ProfileFriend::getFriend).map(Profile::getUsername).toList();
        }
        return new ArrayList<>();
    }

    public void addFriendByUsernames(String profile_username, String friend_username) {
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));
        ProfileFriend friends = ProfileFriend.builder().profile(profile).friend(friend).build();
        friendsRepository.save(friends);
    }


}
