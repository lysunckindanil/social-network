package org.example.friendpostsservice.function;

import lombok.RequiredArgsConstructor;
import org.example.friendpostsservice.dto.PostDto;
import org.example.friendpostsservice.service.FriendPostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class FriendPostsFunction {
    private final FriendPostService friendPostService;

    @Bean
    public Function<String, List<PostDto>> getByUsername() {
        return friendPostService::getFriendsPosts;
    }
}
