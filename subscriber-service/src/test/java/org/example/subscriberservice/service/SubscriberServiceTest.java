package org.example.subscriberservice.service;

import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.model.Profile;
import org.example.subscriberservice.repo.ProfileRepository;
import org.example.subscriberservice.repo.ProfileSubscriberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubscriberServiceTest {

    @Autowired
    private ProfileSubscriberRepository profileSubscriberRepository;
    @Autowired
    private ProfileRepository profileRepository;

    private SubscriberService subscriberService;

    @BeforeAll
    void setUp() {
        subscriberService = new SubscriberService(profileSubscriberRepository, profileRepository);
    }

    @Test
    void getSubscribers_AddSubscriber_ReturnsSubscribers() {
        profileRepository.save(Profile.builder().username("user1").password("p").build());
        profileRepository.save(Profile.builder().username("user2").password("p").build());
        profileRepository.save(Profile.builder().username("user3").password("p").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profile_username("user1").subscriber_username("user2").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profile_username("user1").subscriber_username("user3").build());
        Assertions.assertEquals(2, subscriberService.getSubscribers("user1").size());
    }

    @Test
    void getProfileSubscribedOn() {
        profileRepository.save(Profile.builder().username("user1").password("p").build());
        profileRepository.save(Profile.builder().username("user2").password("p").build());
        profileRepository.save(Profile.builder().username("user3").password("p").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profile_username("user2").subscriber_username("user1").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profile_username("user3").subscriber_username("user1").build());
        Assertions.assertEquals(2, subscriberService.getProfileSubscribedOn("user1").size());
    }

    @Test
    void getSubscribers_AddVerySubscriberMoreThanOne_AddsOnlyOne() {
        profileRepository.save(Profile.builder().username("user1").password("p").build());
        profileRepository.save(Profile.builder().username("user2").password("p").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profile_username("user1").subscriber_username("user2").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profile_username("user1").subscriber_username("user2").build());
        Assertions.assertEquals(1, subscriberService.getSubscribers("user1").size());
    }


    @Test
    void deleteSubscriber_DeleteSubscriber_SubscriberDeleted() {
        profileRepository.save(Profile.builder().username("user1").password("p").build());
        profileRepository.save(Profile.builder().username("user2").password("p").build());
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder().profile_username("user1").subscriber_username("user2").build();
        subscriberService.addSubscriber(dto);
        subscriberService.deleteSubscriber(dto);
        Assertions.assertEquals(0, subscriberService.getSubscribers("user1").size());
    }
}