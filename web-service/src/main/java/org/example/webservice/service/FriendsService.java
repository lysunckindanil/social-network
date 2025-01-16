package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.AddAndDeleteFriendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FriendsService {
    private final FriendsServiceClient friendsServiceClient;

    public List<String> getSubscribing(String username) {
        return friendsServiceClient.getSubscribing(username);
    }

    public List<String> getSubscribed(String username) {
        return friendsServiceClient.getSubscribed(username);
    }

    public Boolean isISubscribedOn(String profile_username, String username) {
        List<String> friends = friendsServiceClient.getSubscribing(profile_username);
        return friends.contains(username);
    }

    public void addFriend(String profile_username, String friend_username) {
        if (!friend_username.equals(profile_username)) {
            AddAndDeleteFriendDto dto = AddAndDeleteFriendDto.builder()
                    .profile_username(profile_username)
                    .friend_username(friend_username)
                    .build();
            friendsServiceClient.addFriend(dto);
        }
    }

    public void deleteFriend(String profile_username, String friend_username) {
        if (!friend_username.equals(profile_username)) {
            AddAndDeleteFriendDto dto = AddAndDeleteFriendDto.builder()
                    .profile_username(profile_username)
                    .friend_username(friend_username)
                    .build();
            friendsServiceClient.deleteFriend(dto);
        }
    }

    @FeignClient(name = "friends-service", path = "friends-service")
    interface FriendsServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/getSubscribed")
        List<String> getSubscribed(@RequestBody String username);

        @RequestMapping(method = RequestMethod.POST, value = "/getSubscribing")
        List<String> getSubscribing(@RequestBody String username);

        @RequestMapping(method = RequestMethod.POST, value = "/addFriend")
        void addFriend(@RequestBody AddAndDeleteFriendDto dto);

        @RequestMapping(method = RequestMethod.POST, value = "/deleteFriend")
        void deleteFriend(@RequestBody AddAndDeleteFriendDto dto);
    }
}
