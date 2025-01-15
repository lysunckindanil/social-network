package org.example.friends_service.service;

import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.repo.ProfileSubscribedRepository;
import org.example.friends_service.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubscribedServiceTest {
    @Autowired
    private ProfileSubscribedRepository friendsRepository;
    @Autowired
    private ProfileRepository profileRepository;

    private SubscribedService subscribedService;


    @BeforeEach
    public void setUp() {
        this.subscribedService = new SubscribedService(friendsRepository, profileRepository);
    }

    @Test
    void addFriendByUsernames_AddFriend_ReturnsSubscribed() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        subscribedService.addSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{"user1"}, subscribedService.getSubscribedUsernamesByProfileUsername("user2").toArray());
    }

    @Test
    void addFriendByUsernames_AddOneFriendDouble_AddsOnlyOneSubscribed() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        subscribedService.addSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        subscribedService.addSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{"user1"}, subscribedService.getSubscribedUsernamesByProfileUsername("user2").toArray());
    }

    @Test
    void addFriendByUsernames_AddSomeSubscribed_ReturnsFriends() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        profileRepository.save(Profile.builder().username("user3").build());
        subscribedService.addSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user3").build());
        subscribedService.addSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user2").friend_username("user3").build());

        Assertions.assertArrayEquals(new String[]{"user1", "user2"}, subscribedService.getSubscribedUsernamesByProfileUsername("user3").toArray());
    }

    @Test
    void deleteFriendByUsernames_DeleteFriend_DeletesSubscribed() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        subscribedService.addSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        subscribedService.deleteSubscribedByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{}, subscribedService.getSubscribedUsernamesByProfileUsername("user2").toArray());
    }
}