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
        Optional<Profile> optionalProfile = profileRepository.findByUsername(username);
        if (optionalProfile.isPresent()) {
            Optional<ProfileFriend> friends = friendsRepository.findProfileFriendByProfile(optionalProfile.get());
            if (friends.isPresent()) {
                return friends.get().getFriends().stream().map(Profile::getUsername).toList();
            }
        }
        return new ArrayList<>();
    }

    public void addFriendByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileFriend> friends_optional = friendsRepository.findProfileFriendByProfile(profile);
        if (friends_optional.isPresent()) {
            friends_optional.get().addFriend(friend);
            friendsRepository.save(friends_optional.get());
        } else {
            ProfileFriend friends_new = new ProfileFriend();
            friends_new.setProfile(profile);
            friends_new.addFriend(friend);
            friendsRepository.save(friends_new);
        }
    }

    public void deleteFriendsByUsernames(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileFriend> friends_optional = friendsRepository.findProfileFriendByProfile(profile);
        if (friends_optional.isPresent()) {
            friends_optional.get().deleteFriend(friend);
            friendsRepository.save(friends_optional.get());
        }
    }


}
