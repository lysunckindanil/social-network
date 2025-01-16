package org.example.friends_service.function;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.service.SubscribedByService;
import org.example.friends_service.service.SubscribedOnService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class FriendsFunction {
    private final SubscribedByService subscribedByService;
    private final SubscribedOnService subscribedOnService;

    @Bean
    public Function<String, List<String>> getSubscribed() {
        return subscribedByService::getSubscribedUsernamesByProfileUsername;
    }


    @Bean
    public Function<String, List<String>> getSubscribing() {
        return subscribedOnService::getSubscribingUsernamesByProfileUsername;
    }

    @Bean
    public Consumer<AddAndDeleteFriendDto> addFriend() {
        return (dto) -> {
            subscribedByService.addSubscribedOnProfile(dto); // a, b; a subscribed on b, b subscribing on a
            subscribedOnService.makeProfileSubscribedOn(dto);
        };
    }

    @Bean
    public Consumer<AddAndDeleteFriendDto> deleteFriend() {
        return (dto) -> {
            subscribedByService.deleteSubscribedFromProfile(dto); // a, b; a isn't subscribed by b, b is ont subscribing on a
            subscribedOnService.deleteProfileSubscribeOn(dto);
        };
    }
}
