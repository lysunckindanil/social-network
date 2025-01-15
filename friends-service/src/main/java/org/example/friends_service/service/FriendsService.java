package org.example.friends_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileFriend;
import org.example.friends_service.repo.ProfileFriendRepository;
import org.example.friends_service.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendsService {
    private final ProfileFriendRepository friendsRepository;
    private final ProfileRepository profileRepository;

    public List<String> getFriendsUsernamesByProfileUsername(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfileFriend> friends = friendsRepository.findProfileFriendByProfile(profile);
        if (friends.isPresent()) {
            return friends.get().getFriends().stream().map(Profile::getUsername).toList();
        }
        return new ArrayList<>();
    }

    public void addFriendByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileFriend> friends_optional = friendsRepository.findProfileFriendByProfile(friend);
        if (friends_optional.isPresent()) {
            ProfileFriend friends = friends_optional.get();
            friends.addFriend(profile);
            friendsRepository.save(friends);
        } else {
            ProfileFriend friends_new = new ProfileFriend();
            friends_new.setProfile(friend);
            friends_new.addFriend(profile);
            friendsRepository.save(friends_new);
        }
    }

    public void deleteFriendsByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileFriend> friends_optional = friendsRepository.findProfileFriendByProfile(friend);
        if (friends_optional.isPresent()) {
            ProfileFriend friends = friends_optional.get();
            friends.deleteFriend(profile);
            friendsRepository.save(friends);
        }
    }
}
