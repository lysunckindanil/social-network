package org.example.subscriberpostsservice.function;

import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.service.SubscribersPostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class SubscriberPostFunction {
    private final SubscribersPostService subscribersPostService;

    @Bean
    public Function<String, List<PostDto>> getByUsername() {
        return subscribersPostService::getSubscribersPosts;
    }
}
