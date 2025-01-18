package org.example.sharepostsservice.service;

import org.example.sharepostsservice.dto.DeletePostDto;
import org.example.sharepostsservice.dto.ShareSubscribersDto;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileSubscriber;
import org.example.sharepostsservice.repo.PostRepository;
import org.example.sharepostsservice.repo.PostSubscriberRepository;
import org.example.sharepostsservice.repo.ProfileRepository;
import org.example.sharepostsservice.repo.ProfileSubscriberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SharePostServiceTest {

    @Autowired
    private PostSubscriberRepository postSubscriberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileSubscriberRepository profileSubscriberRepository;

    private SharePostService sharePostService;

    @BeforeAll
    void setUp() {
        sharePostService = new SharePostService(postRepository, profileRepository, profileSubscriberRepository, postSubscriberRepository);
    }

    @Test
    void sharePostsToFriends_ProfilePosts_FriendsGetPosts() {
        Profile profile = Profile.builder().username("user1").password("p").build();
        Profile subscriber = Profile.builder().username("user2").password("p").build();
        Long profile_id = profileRepository.save(profile).getId();
        profileRepository.save(subscriber);
        profileSubscriberRepository.save(ProfileSubscriber.builder().profile(profile).subscriber(subscriber).build());

        Post post = Post.builder().label("l").content("c").createdAt(LocalDateTime.now()).build();
        postRepository.save(post);

        ShareSubscribersDto dto = ShareSubscribersDto.builder().profile_id(profile_id).post_id(post.getId()).build();

        sharePostService.sharePostsToSubscribers(dto);
        Assertions.assertEquals(post.getId(), postSubscriberRepository.findAll().getFirst().getPost().getId());
        Assertions.assertEquals(subscriber.getId(), postSubscriberRepository.findAll().getFirst().getSubscriber().getId());
    }


    @Test
    void deletePostsFromFriends_ProfileDeletes_PostsDeleted() {
        Profile profile = Profile.builder().username("user1").password("p").build();
        Profile subscriber = Profile.builder().username("user2").password("p").build();
        Long profile_id = profileRepository.save(profile).getId();
        profileRepository.save(subscriber);
        profileSubscriberRepository.save(ProfileSubscriber.builder().profile(profile).subscriber(subscriber).build());

        Post post = Post.builder().label("l").content("c").createdAt(LocalDateTime.now()).build();
        postRepository.save(post);

        ShareSubscribersDto dto = ShareSubscribersDto.builder().profile_id(profile_id).post_id(post.getId()).build();

        sharePostService.sharePostsToSubscribers(dto);
        sharePostService.deletePostsFromSubscribers(DeletePostDto.builder().post_id(post.getId()).build());


        Assertions.assertEquals(0, postSubscriberRepository.findAll().size());
        Assertions.assertEquals(0, postRepository.findAll().size());
    }
}