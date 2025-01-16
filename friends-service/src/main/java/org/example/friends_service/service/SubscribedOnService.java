package org.example.friends_service.service;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribeOn;
import org.example.friends_service.repo.ProfileRepository;
import org.example.friends_service.repo.ProfileSubscribedOnRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * In repositories stored entity with the structure:
 * Profile - profile A
 * List<Profile> with OneToMany - profiles who profile A subscribed on
 */

@RequiredArgsConstructor
@Service
public class SubscribedOnService {
    private final ProfileSubscribedOnRepository profileSubscribedOnRepository;
    private final ProfileRepository profileRepository;

    /**
     * @param username - username of profile
     * @return returns list of usernames of profiles who profile subscribed on
     */
    public List<String> getSubscribingUsernamesByProfileUsername(String username) {
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfileSubscribeOn> friends = profileSubscribedOnRepository.findProfileSubscribingByProfile(profile);
        if (friends.isPresent()) {
            return friends.get().getProfiles().stream().map(Profile::getUsername).toList();
        }
        return new ArrayList<>();
    }

    /**
     * @param dto - profile_username - the subscriber,
     * friend_username - the profile subscribed by the former
     */
    public void makeProfileSubscribedOn(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribeOn> friends_optional = profileSubscribedOnRepository.findProfileSubscribingByProfile(profile);
        if (friends_optional.isPresent()) {
            ProfileSubscribeOn subscribing = friends_optional.get();
            subscribing.addProfile(friend);
            profileSubscribedOnRepository.save(subscribing);
        } else {
            ProfileSubscribeOn subscribing_new = new ProfileSubscribeOn();
            subscribing_new.setProfile(profile);
            subscribing_new.addProfile(friend);
            profileSubscribedOnRepository.save(subscribing_new);
        }
    }


    /**
     * @param dto - profile_username - the subscriber,
     * friend_username - the profile subscribed by the former
     * the former will not be subscribed on the later any more
     */
    public void deleteProfileSubscribeOn(AddAndDeleteFriendDto dto) {
        String profile_username = dto.getProfile_username();
        String friend_username = dto.getFriend_username();
        Profile profile = profileRepository.findByUsername(profile_username).orElseThrow(() -> new RuntimeException("User is not found"));
        Profile friend = profileRepository.findByUsername(friend_username).orElseThrow(() -> new RuntimeException("User is not found"));

        Optional<ProfileSubscribeOn> friends_optional = profileSubscribedOnRepository.findProfileSubscribingByProfile(profile);
        if (friends_optional.isPresent()) {
            ProfileSubscribeOn subscribing = friends_optional.get();
            subscribing.deleteProfile(friend);
            profileSubscribedOnRepository.save(subscribing);
        }
    }
}
