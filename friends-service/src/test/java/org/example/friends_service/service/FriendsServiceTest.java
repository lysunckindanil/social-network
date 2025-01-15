package org.example.friends_service.service;

import org.example.friends_service.dto.AddAndDeleteFriendDto;
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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
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
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{"user2"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void addFriendByUsernames_AddOneFriendDouble_AddsOnlyOneFriend() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{"user2"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void addFriendByUsernames_AddSomeFriend_ReturnsFriends() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        profileRepository.save(Profile.builder().username("user3").build());
        profileRepository.save(Profile.builder().username("user4").build());
        profileRepository.save(Profile.builder().username("user5").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user3").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user4").friend_username("user5").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user4").friend_username("user3").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user5").friend_username("user1").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user3").friend_username("user1").build());

        Assertions.assertArrayEquals(new String[]{"user2", "user3","user5"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
        Assertions.assertArrayEquals(new String[]{"user5", "user3"}, friendsService.getFriendsUsernamesByProfileUsername("user4").toArray());
        Assertions.assertArrayEquals(new String[]{"user4","user1"}, friendsService.getFriendsUsernamesByProfileUsername("user5").toArray());
        Assertions.assertArrayEquals(new String[]{"user1","user4"}, friendsService.getFriendsUsernamesByProfileUsername("user3").toArray());
    }

    @Test
    void addFriendByUsernames_AddFriend_ResultViceVersaFriends() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user2").friend_username("user1").build());

        Assertions.assertArrayEquals(new String[]{"user1"}, friendsService.getFriendsUsernamesByProfileUsername("user2").toArray());
        Assertions.assertArrayEquals(new String[]{"user2"}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void deleteFriendByUsernames_DeleteFriend_DeletesFriend() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        friendsService.deleteFriendsByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
    }

    @Test
    void deleteFriendByUsernames_DeleteFriend_DeletesFriendViceVersa() {
        profileRepository.save(Profile.builder().username("user1").build());
        profileRepository.save(Profile.builder().username("user2").build());
        friendsService.addFriendByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        friendsService.deleteFriendsByUsernames(AddAndDeleteFriendDto.builder().profile_username("user1").friend_username("user2").build());
        Assertions.assertArrayEquals(new String[]{}, friendsService.getFriendsUsernamesByProfileUsername("user1").toArray());
        Assertions.assertArrayEquals(new String[]{}, friendsService.getFriendsUsernamesByProfileUsername("user2").toArray());
    }
}