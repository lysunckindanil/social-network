package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.AddAndDeleteSubscriberDto;
import org.example.webservice.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscriberService {
    private final FriendsServiceClient friendsServiceClient;

    public List<ProfileDto> findSubscribers(String username) {
        return friendsServiceClient.findSubscribers(username);
    }

    public List<ProfileDto> findProfileSubscribedOn(String username) {
        return friendsServiceClient.findProfileSubscribedOn(username);
    }

    public Boolean isISubscribedOn(String profile_username, String username) {
        List<String> friends = friendsServiceClient.findSubscribers(profile_username).stream().map(ProfileDto::getUsername).toList();
        return friends.contains(username);
    }

    public void addFriend(String profile_username, String friend_username) {
        if (!friend_username.equals(profile_username)) {
            AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                    .profile_username(profile_username)
                    .friend_username(friend_username)
                    .build();
            friendsServiceClient.addSubscriber(dto);
        }
    }

    public void deleteFriend(String profile_username, String friend_username) {
        if (!friend_username.equals(profile_username)) {
            AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                    .profile_username(profile_username)
                    .friend_username(friend_username)
                    .build();
            friendsServiceClient.deleteSubscriber(dto);
        }
    }

    @FeignClient(name = "subscriber-service", path = "subscriber-service")
    interface FriendsServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/findSubscribers")
        List<ProfileDto> findSubscribers(@RequestBody String username);

        @RequestMapping(method = RequestMethod.POST, value = "/findProfileSubscribedOn")
        List<ProfileDto> findProfileSubscribedOn(@RequestBody String username);

        @RequestMapping(method = RequestMethod.POST, value = "/addSubscriber")
        void addSubscriber(@RequestBody AddAndDeleteSubscriberDto dto);

        @RequestMapping(method = RequestMethod.POST, value = "/deleteSubscriber")
        void deleteSubscriber(@RequestBody AddAndDeleteSubscriberDto dto);
    }
}
