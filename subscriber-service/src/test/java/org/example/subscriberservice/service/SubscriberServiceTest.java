package org.example.subscriberservice.service;

import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.model.Profile;
import org.example.subscriberservice.repo.ProfileRepository;
import org.example.subscriberservice.repo.ProfileSubscriberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubscriberServiceTest {

    @Autowired
    private ProfileSubscriberRepository profileSubscriberRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private SubscriberService subscriberService;


    @BeforeEach
    void beforeEach() {
        profileSubscriberRepository.deleteAll();
        profileRepository.deleteAll();
        Profile profile = new Profile();
        profile.setUsername("user1");
        profile.setPassword("p");
        profileRepository.save(profile);

        profile = new Profile();
        profile.setUsername("user2");
        profile.setPassword("p");
        profileRepository.save(profile);

        profile = new Profile();
        profile.setUsername("user3");
        profile.setPassword("p");
        profileRepository.save(profile);
    }

    @Test
    void getSubscribers_AddSubscriber_ReturnsSubscribers() {
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profileUsername("user1").subscriberUsername("user2").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profileUsername("user1").subscriberUsername("user3").build());

        Assertions.assertEquals(2, profileSubscriberRepository.findAll().size());
        Assertions.assertEquals(2, subscriberService.getSubscribers("user1").size());
    }

    @Test
    void getProfileSubscribedOn() {
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profileUsername("user2").subscriberUsername("user1").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profileUsername("user3").subscriberUsername("user1").build());
        Assertions.assertEquals(2, subscriberService.getProfileSubscribedOn("user1").size());
    }

    @Test
    void getSubscribers_AddVerySubscriberMoreThanOne_AddsOnlyOne() {
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profileUsername("user1").subscriberUsername("user2").build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder().profileUsername("user1").subscriberUsername("user2").build());
        Assertions.assertEquals(1, subscriberService.getSubscribers("user1").size());
    }


    @Test
    void deleteSubscriber_DeleteSubscriber_SubscriberDeleted() {
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder().profileUsername("user1").subscriberUsername("user2").build();
        subscriberService.addSubscriber(dto);
        subscriberService.deleteSubscriber(dto);
        Assertions.assertEquals(0, profileSubscriberRepository.count());
        Assertions.assertEquals(0, subscriberService.getSubscribers("user1").size());
    }
}