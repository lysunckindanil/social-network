package org.example.friends_service.function;

import lombok.RequiredArgsConstructor;
import org.example.friends_service.dto.AddFriendDto;
import org.example.friends_service.service.FriendsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class FriendsFunction {
    private final FriendsService friendsService;

    @Bean
    public Function<String, List<String>> getFriends() {
        return friendsService::getFriendsUsernamesByProfileUsername;
    }

    @Bean
    public Consumer<AddFriendDto> addFriend() {
        return (dto) -> friendsService.addFriendByUsernames(dto.getProfile_username(), dto.getFriend_username());
    }
}
