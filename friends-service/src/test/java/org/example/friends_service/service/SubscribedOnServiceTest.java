package org.example.friends_service.service;

import org.example.friends_service.dto.AddAndDeleteFriendDto;
import org.example.friends_service.model.Profile;
import org.example.friends_service.repo.ProfileRepository;
import org.example.friends_service.repo.ProfileSubscribedOnRepository;
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
class SubscribedOnServiceTest {

    @Autowired
    private ProfileSubscribedOnRepository profileSubscribedOnRepository;
    @Autowired
    private ProfileRepository profileRepository;

    private SubscribedOnService subscribedOnService;


    @BeforeEach
    public void setUp() {
        this.subscribedOnService = new SubscribedOnService(profileSubscribedOnRepository, profileRepository);
    }

    @Test
    void makeProfileSubscribedOn_MakeSubscriber_AddsToRepository() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        subscribedOnService.makeProfileSubscribedOn(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{"user2"}, subscribedOnService.getSubscribingUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void makeProfileSubscribedOn_MakeSubscriberDouble_AddsToRepositoryOne() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        subscribedOnService.makeProfileSubscribedOn(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        subscribedOnService.makeProfileSubscribedOn(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{"user2"}, subscribedOnService.getSubscribingUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void deleteSubscribedOnByUsernames_DeleteSubscribed_DeletesSubscribing() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        subscribedOnService.makeProfileSubscribedOn(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        subscribedOnService.deleteProfileSubscribeOn(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{}, subscribedOnService.getSubscribingUsernamesByProfileUsername("user1").toArray());
    }
}