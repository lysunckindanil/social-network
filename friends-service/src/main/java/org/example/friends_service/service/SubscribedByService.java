package org.example.friends_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribedBy;
import org.example.friends_service.repo.ProfileSubscribedByRepository;
import org.example.friends_service.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In repositories stored entity with the structure:
 * Profile - profile A
 * List<Profile> with OneToMany - profiles who subscribed on profile A
 */


@Slf4j
@RequiredArgsConstructor
@Service
public class SubscribedByService {
    private final ProfileSubscribedByRepository profileSubscribedByRepository;
    private final ProfileRepository profileRepository;

    /**
     * @param username - username of profile
     * @return returns list of usernames of profiles who subscribed on the profile
     */

    public List<String> getSubscribedUsernamesByProfileUsername(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfileSubscribedBy> friends = profileSubscribedByRepository.findProfileSubscribersByProfile(profile);
        if (friends.isPresent()) {
            return friends.get().getProfiles().stream().map(Profile::getUsername).toList();
        }
        return new ArrayList<>();
    }

    /**
     * @param dto - two usernames, profile_username - profile who subscribes,
     *            friend_username - profile on who subscribed the former
     */
    public void addSubscribedOnProfile(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribedBy> friends_optional = profileSubscribedByRepository.findProfileSubscribersByProfile(friend);
        if (friends_optional.isPresent()) {
            ProfileSubscribedBy friends = friends_optional.get();
            friends.addProfile(profile);
            profileSubscribedByRepository.save(friends);
        } else {
            ProfileSubscribedBy friends_new = new ProfileSubscribedBy();
            friends_new.setProfile(friend);
            friends_new.addProfile(profile);
            profileSubscribedByRepository.save(friends_new);
        }
    }


    public void deleteSubscribedFromProfile(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribedBy> friends_optional = profileSubscribedByRepository.findProfileSubscribersByProfile(friend);
        if (friends_optional.isPresent()) {
            ProfileSubscribedBy friends = friends_optional.get();
            friends.deleteProfile(profile);
            profileSubscribedByRepository.save(friends);
        }
    }
}
