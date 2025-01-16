package org.example.sharepostsservice.service;

import org.example.sharepostsservice.dto.ShareFriendsDto;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileSubscribedBy;
import org.example.sharepostsservice.repo.ProfileSubscribedByPostRepository;
import org.example.sharepostsservice.repo.PostRepository;
import org.example.sharepostsservice.repo.ProfileSubscribedByRepository;
import org.example.sharepostsservice.repo.ProfileRepository;
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
    private ProfileSubscribedByPostRepository profileSubscribedByPostRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileSubscribedByRepository profileSubscribedByRepository;

    private SharePostService sharePostService;

    @BeforeEach
    void setUp() {
        sharePostService = new SharePostService(postRepository, profileRepository, profileSubscribedByRepository, profileSubscribedByPostRepository);
    }

    @Test
    void sharePostsToFriends_ProfilePosts_FriendsGetPosts() {
        Profile profile = Profile.builder().username("user1").build();
        Profile friend1 = Profile.builder().username("user2").build();
        Profile friend2 = Profile.builder().username("user3").build();
        Long profile_id = profileRepository.save(profile).getId();
        profileRepository.save(friend1);
        profileRepository.save(friend2);

        ProfileSubscribedBy profileSubscribedBy = new ProfileSubscribedBy();
        profileSubscribedBy.setProfile(profile);
        profileSubscribedBy.addFriend(friend1);
        profileSubscribedBy.addFriend(friend2);
        profileSubscribedByRepository.save(profileSubscribedBy);

        Post post = Post.builder().build();
        postRepository.save(post);

        ShareFriendsDto dto = ShareFriendsDto.builder().profile_id(profile_id).post_id(post.getId()).build();

        sharePostService.sharePostsToFriends(dto);

        Assertions.assertEquals(1, profileSubscribedByPostRepository.findProfileSubscribedByPosts(friend1).get().getPosts().size());
        Assertions.assertEquals(1, profileSubscribedByPostRepository.findProfileSubscribedByPosts(friend2).get().getPosts().size());
    }

    @Test
    void deletePostsFromFriends_ProfilePosts_FriendsGetPosts_ProfileDeletes_PostsDeleted() {
        Profile profile = Profile.builder().username("user1").build();
        Profile friend1 = Profile.builder().username("user2").build();
        Profile friend2 = Profile.builder().username("user3").build();
        Long profile_id = profileRepository.save(profile).getId();
        profileRepository.save(friend1);
        profileRepository.save(friend2);

        ProfileSubscribedBy profileSubscribedBy = new ProfileSubscribedBy();
        profileSubscribedBy.setProfile(profile);
        profileSubscribedBy.addFriend(friend1);
        profileSubscribedBy.addFriend(friend2);
        profileSubscribedByRepository.save(profileSubscribedBy);

        Post post = Post.builder().build();
        postRepository.save(post);

        ShareFriendsDto dto = ShareFriendsDto.builder().profile_id(profile_id).post_id(post.getId()).build();

        sharePostService.sharePostsToFriends(dto);
        sharePostService.deletePostsFromFriends(dto);

        Assertions.assertEquals(0, profileSubscribedByPostRepository.findProfileSubscribedByPosts(friend1).get().getPosts().size());
        Assertions.assertEquals(0, profileSubscribedByPostRepository.findProfileSubscribedByPosts(friend2).get().getPosts().size());
        Assertions.assertFalse(postRepository.findById(post.getId()).isPresent());
    }
}