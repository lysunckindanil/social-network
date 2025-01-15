package org.example.friendpostsservice.function;

import lombok.RequiredArgsConstructor;
import org.example.friendpostsservice.dto.PostsDto;
import org.example.friendpostsservice.service.FriendPostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class FriendPostsFunction {
    private final FriendPostService friendPostService;

    @Bean
    public Function<String, PostsDto> getByUsername() {
        return friendPostService::getFriendsPosts;
    }
}
