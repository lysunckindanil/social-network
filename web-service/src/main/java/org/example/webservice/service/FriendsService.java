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

    public List<String> getFriends(String username) {
        return friendsServiceClient.getFriends(username);
    }

    public Boolean isMyFriend(String profile_username, String username) {
        List<String> friends = getFriends(profile_username);
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

    @FeignClient(name = "friends-service", url = "http://192.168.0.100:8000", path = "friends-service")
    interface FriendsServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/getFriends")
        List<String> getFriends(@RequestBody String username);

        @RequestMapping(method = RequestMethod.POST, value = "/addFriend")
        void addFriend(@RequestBody AddAndDeleteFriendDto dto);

        @RequestMapping(method = RequestMethod.POST, value = "/deleteFriend")
        void deleteFriend(@RequestBody AddAndDeleteFriendDto dto);
    }
}
