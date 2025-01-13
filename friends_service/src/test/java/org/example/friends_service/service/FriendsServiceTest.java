package org.example.friends_service.service;

import org.example.friends_service.model.Profile;
import org.example.friends_service.repo.ProfileFriendRepository;
import org.example.friends_service.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FriendsServiceTest {
    @Autowired
    private ProfileFriendRepository friendsRepository;
    @Autowired
    private ProfileRepository profileRepository;

    private FriendsService friendsService;



    @BeforeEach
    public void setUp() {
        this.friendsService = new FriendsService(friendsRepository, profileRepository);
    }

    @Test
    void addFriendByUsernames_AddFriend_ReturnsFriend() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames("user1", "user2");
        Assertions.assertArrayEquals(new String[]{"user2"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void addFriendByUsernames_AddSomeFriend_ReturnsFriends() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        profileRepository.save(Profile.builder().username("user3").build());
        friendsService.addFriendByUsernames("user1", "user2");
        friendsService.addFriendByUsernames("user1", "user3");
        Assertions.assertArrayEquals(new String[]{"user2", "user3"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void addFriendByUsernames_AddFriend_ResultViceVersaNotFriends() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames("user1", "user2");
        Assertions.assertArrayEquals(new String[]{}, friendsService.getFriendsUsernamesByProfileUsername("user2").toArray());
    }

    @Test
    void addFriendByUsernames_AddFriend_AndViceVersa_ResultViceVersaFriends() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames("user1", "user2");
        friendsService.addFriendByUsernames("user2", "user1");
        Assertions.assertArrayEquals(new String[]{"user1"}, friendsService.getFriendsUsernamesByProfileUsername("user2").toArray());
        Assertions.assertArrayEquals(new String[]{"user2"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void deleteFriendByUsernames_DeleteFriend_DeletesFriend() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames("user1", "user2");
        friendsService.deleteFriendsByUsernames("user1", "user2");
        Assertions.assertArrayEquals(new String[]{}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }
}