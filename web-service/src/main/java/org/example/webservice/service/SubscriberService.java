package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.AddAndDeleteSubscriberDto;
import org.example.webservice.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    public Boolean isISubscribedOn(String subscriberUsername, String profileUsername) {
        List<String> friends = friendsServiceClient.findSubscribers(profileUsername).stream().map(ProfileDto::getUsername).toList();
        return friends.contains(subscriberUsername);
    }

    public void subscribe(String profileUsername, String subscriberUsername) {
        if (!subscriberUsername.equals(profileUsername)) {
            AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                    .profileUsername(profileUsername)
                    .subscriberUsername(subscriberUsername)
                    .build();
            friendsServiceClient.addSubscriber(dto);
        }
    }

    public void unsubscribe(String profileUsername, String subscriberUsername) {
        if (!subscriberUsername.equals(profileUsername)) {
            AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                    .profileUsername(profileUsername)
                    .subscriberUsername(subscriberUsername)
                    .build();
            friendsServiceClient.deleteSubscriber(dto);
        }
    }

    @FeignClient(name = "subscriber-service", path = "subscriber-service")
    interface FriendsServiceClient {
        @PostMapping("/findSubscribers")
        List<ProfileDto> findSubscribers(@RequestBody String username);

        @PostMapping("/findProfileSubscribedOn")
        List<ProfileDto> findProfileSubscribedOn(@RequestBody String username);

        @PostMapping("/addSubscriber")
        void addSubscriber(@RequestBody AddAndDeleteSubscriberDto dto);

        @PostMapping("/deleteSubscriber")
        void deleteSubscriber(@RequestBody AddAndDeleteSubscriberDto dto);
    }
}
