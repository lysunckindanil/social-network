package org.example.sharepostsservice.service;

import org.example.sharepostsservice.dto.DeletePostDto;
import org.example.sharepostsservice.dto.ShareSubscribersDto;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.PostSubscriber;
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
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        Profile subscriber = new Profile();
        subscriber.setUsername("u1");
        subscriber.setPassword("p");
        profileRepository.save(subscriber);

        Long profileId = profileRepository.save(profile).getId();
        profileRepository.save(subscriber);
        profileSubscriberRepository.save(new ProfileSubscriber(profile, subscriber));

        Post post = new Post();
        post.setLabel("l");
        post.setContent("c");
        postRepository.save(post);

        ShareSubscribersDto dto = ShareSubscribersDto.builder().profileId(profileId).postId(post.getId()).build();

        sharePostService.sharePostsToSubscribers(dto);
        PostSubscriber ps = postSubscriberRepository.findAll().getFirst();

        Assertions.assertEquals(post.getId(), ps.getPost().getId());
        Assertions.assertEquals(subscriber.getId(), ps.getSubscriber().getId());
    }


    @Test
    void deletePostsFromFriends_ProfileDeletes_PostsDeleted() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        Long profileId = profileRepository.save(profile).getId();

        Profile subscriber = new Profile();
        subscriber.setUsername("u1");
        subscriber.setPassword("p");
        profileRepository.save(subscriber);
        profileRepository.save(subscriber);
        profileSubscriberRepository.save(new ProfileSubscriber(profile, subscriber));

        Post post = new Post();
        post.setLabel("l");
        post.setContent("c");
        postRepository.save(post);

        ShareSubscribersDto dto = ShareSubscribersDto.builder().profileId(profileId).postId(post.getId()).build();

        sharePostService.sharePostsToSubscribers(dto);
        Assertions.assertEquals(1, postSubscriberRepository.findAll().size());
        Assertions.assertEquals(1, postRepository.findAll().size());
        sharePostService.deletePostsFromSubscribers(DeletePostDto.builder().postId(post.getId()).build());


        Assertions.assertEquals(0, postSubscriberRepository.findAll().size());
        Assertions.assertEquals(0, postRepository.findAll().size());
    }
}