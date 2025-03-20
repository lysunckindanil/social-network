package org.example.subscriberservice.service;

import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.dto.GetSubscribersPageableDto;
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
    void findProfileSubscribedByPageable() {
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user2")
                .build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user3")
                .build());

        Assertions.assertEquals(2, profileSubscriberRepository.findAll().size());
        Assertions.assertEquals(2, subscriberService.findProfileSubscribedByPageable(GetSubscribersPageableDto
                        .builder()
                        .profileUsername("user1")
                        .page(0)
                        .size(5)
                        .build())
                .size());
    }

    @Test
    void findProfileSubscribedByPageable_UserDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.findProfileSubscribedByPageable(
                GetSubscribersPageableDto.builder()
                        .profileUsername("user10")
                        .page(1)
                        .size(1)
                        .build()));
    }


    @Test
    void findProfileSubscribedOnPageable() {
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user2")
                .subscriberUsername("user1")
                .build());
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user3")
                .subscriberUsername("user1")
                .build());
        Assertions.assertEquals(2, subscriberService.findProfileSubscribedOnPageable(GetSubscribersPageableDto
                        .builder()
                        .profileUsername("user1")
                        .page(0)
                        .size(5)
                        .build())
                .size());

    }

    @Test
    void findProfileSubscribedOnPageable_UserDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.findProfileSubscribedOnPageable(
                GetSubscribersPageableDto.builder()
                        .profileUsername("user10")
                        .page(1)
                        .size(1)
                        .build()));
    }

    @Test
    void addSubscriber_RelationYetExists_ThrowsException() {
        subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user2")
                .build());

        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user2")
                .build()));
    }

    @Test
    void addSubscriber_UsernamesEquals_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user1")
                .build()));
    }

    @Test
    void addSubscriber_UserDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user10")
                .subscriberUsername("user2")
                .build()));

        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.addSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user30")
                .build()));
    }

    @Test
    void deleteSubscriber_DeleteSubscriber_SubscriberDeleted() {
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user2")
                .build();
        subscriberService.addSubscriber(dto);
        subscriberService.deleteSubscriber(dto);
        Assertions.assertEquals(0, profileSubscriberRepository.count());
        Assertions.assertEquals(0, subscriberService.findProfileSubscribedByPageable(GetSubscribersPageableDto
                        .builder()
                        .profileUsername("user1")
                        .page(0)
                        .size(5)
                        .build())
                .size());
    }

    @Test
    void deleteSubscriber_RelationDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.deleteSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user2")
                .build()));
    }

    @Test
    void deleteSubscriber_UserDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.deleteSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user10")
                .subscriberUsername("user2")
                .build()));

        Assertions.assertThrows(BadRequestException.class, () -> subscriberService.deleteSubscriber(AddAndDeleteSubscriberDto.builder()
                .profileUsername("user1")
                .subscriberUsername("user30")
                .build()));
    }
}