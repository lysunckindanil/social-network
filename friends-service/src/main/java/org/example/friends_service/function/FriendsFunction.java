package org.example.friends_service.function;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.service.SubscribedService;
import org.example.friends_service.service.SubscribingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class FriendsFunction {
    private final SubscribedService subscribedService;
    private final SubscribingService subscribingService;

    @Bean
    public Function<String, List<String>> getSubscribed() {
        return subscribedService::getSubscribedUsernamesByProfileUsername;
    }


    @Bean
    public Function<String, List<String>> getSubscribing() {
        return subscribingService::getSubscribingUsernamesByProfileUsername;
    }

    @Bean
    public Consumer<AddAndDeleteFriendDto> addFriend() {
        return (dto) -> {
            subscribedService.addSubscribedByUsernames(dto); // a, b; a subscribed on b, b subscribing on a
            subscribingService.addSubscribingByUsernames(dto);
        };
    }

    @Bean
    public Consumer<AddAndDeleteFriendDto> deleteFriend() {
        return (dto) -> {
            subscribedService.deleteSubscribedByUsernames(dto); // a, b; a isn't subscribed by b, b is ont subscribing on a
            subscribingService.deleteSubscribingByUsernames(dto);
        };
    }
}
