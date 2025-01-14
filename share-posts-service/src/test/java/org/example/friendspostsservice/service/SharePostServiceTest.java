package org.example.friendspostsservice.service;

import org.example.friendspostsservice.dto.ShareFriendsDto;
import org.example.friendspostsservice.model.Post;
import org.example.friendspostsservice.model.Profile;
import org.example.friendspostsservice.model.ProfileFriend;
import org.example.friendspostsservice.repo.FriendPostRepository;
import org.example.friendspostsservice.repo.PostRepository;
import org.example.friendspostsservice.repo.ProfileFriendRepository;
import org.example.friendspostsservice.repo.ProfileRepository;
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
class SharePostServiceTest {

    @Autowired
    private FriendPostRepository friendPostRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileFriendRepository profileFriendRepository;

    private SharePostService sharePostService;

    @BeforeEach
    void setUp() {
        sharePostService = new SharePostService(postRepository,profileRepository,profileFriendRepository, friendPostRepository);
    }
    @Test
    void sharePostsToFriends_ProfilePosts_FriendsGetPosts() {
        Profile profile = Profile.builder().username("user1").build();
        Profile friend1 = Profile.builder().username("user2").build();
        Profile friend2 = Profile.builder().username("user3").build();
        Long profile_id = profileRepository.save(profile).getId();
        profileRepository.save(friend1);
        profileRepository.save(friend2);

        ProfileFriend profileFriend = new ProfileFriend();
        profileFriend.setProfile(profile);
        profileFriend.addFriend(friend1);
        profileFriend.addFriend(friend2);
        profileFriendRepository.save(profileFriend);

        Post post = Post.builder().build();
        postRepository.save(post);

        ShareFriendsDto dto = ShareFriendsDto.builder().profile_id(profile_id).post_id(post.getId()).build();

        sharePostService.sharePostsToFriends(dto);

        Assertions.assertEquals(1, friendPostRepository.findFriendPostByProfile(friend1).get().getPosts().size());
        Assertions.assertEquals(1, friendPostRepository.findFriendPostByProfile(friend2).get().getPosts().size());
    }
}