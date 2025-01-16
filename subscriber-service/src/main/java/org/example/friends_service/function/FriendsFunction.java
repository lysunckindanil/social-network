package org.example.friends_service.function;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddAndDeleteSubscriberDto;

import org.example.friends_service.dto.ProfileDto;
import org.example.friends_service.service.SubscriberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class FriendsFunction {
    private final SubscriberService subscriberService;

    @Bean
    public Function<String, List<ProfileDto>> findSubscribers() {
        return subscriberService::getSubscribers;
    }

    @Bean
    public Function<String, List<ProfileDto>> findProfileSubscribedOn() {
        return subscriberService::getProfileSubscribedOn;
    }

    @Bean
    public Consumer<AddAndDeleteSubscriberDto> addSubscriber() {
        return subscriberService::addSubscriber;
    }

    @Bean
    public Consumer<AddAndDeleteSubscriberDto> deleteSubscriber() {
        return subscriberService::deleteSubscriber;
    }
}
