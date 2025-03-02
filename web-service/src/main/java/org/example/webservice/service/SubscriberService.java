package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.subscribers.AddAndDeleteSubscriberDto;
import org.example.webservice.dto.subscribers.GetSubscribersPageableDto;
import org.example.webservice.dto.subscribers.IsSubscriberDto;
import org.example.webservice.dto.profiles.ProfileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscriberService {
    private final FriendsServiceClient friendsServiceClient;

    @Value("${profile.service.page_size}")
    private int pageSize;

    public List<ProfileDto> findProfileSubscribedBy(String username, int page) {
        return friendsServiceClient.findProfileSubscribedByPageable(GetSubscribersPageableDto.builder()
                .profileUsername(username)
                .page(page)
                .size(pageSize)
                .build());
    }

    public List<ProfileDto> findProfileSubscribedOn(String username, int page) {
        return friendsServiceClient.findProfileSubscribedOnPageable(GetSubscribersPageableDto.builder()
                .profileUsername(username)
                .page(page)
                .size(pageSize)
                .build());
    }

    public Boolean isISubscribedOn(String subscriberUsername, String profileUsername) {
        return friendsServiceClient.isSubscribedOn(IsSubscriberDto.builder()
                .profileUsername(profileUsername).subscriberUsername(subscriberUsername).build());
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
        @PostMapping("/findProfileSubscribedByPageable")
        List<ProfileDto> findProfileSubscribedByPageable(@RequestBody GetSubscribersPageableDto dto);

        @PostMapping("/findProfileSubscribedOnPageable")
        List<ProfileDto> findProfileSubscribedOnPageable(@RequestBody GetSubscribersPageableDto dto);

        @PostMapping("/addSubscriber")
        void addSubscriber(@RequestBody AddAndDeleteSubscriberDto dto);

        @PostMapping("/deleteSubscriber")
        void deleteSubscriber(@RequestBody AddAndDeleteSubscriberDto dto);

        @PostMapping("/isSubscribedOn")
        Boolean isSubscribedOn(@RequestBody IsSubscriberDto dto);
    }
}
