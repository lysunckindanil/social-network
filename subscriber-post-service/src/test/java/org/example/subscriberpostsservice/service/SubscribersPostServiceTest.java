package org.example.subscriberpostsservice.service;

import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.PostSubscriber;
import org.example.subscriberpostsservice.model.Profile;
import org.example.subscriberpostsservice.repo.PostRepository;
import org.example.subscriberpostsservice.repo.PostSubscriberRepository;
import org.example.subscriberpostsservice.repo.ProfileRepository;
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
import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubscribersPostServiceTest {
    @Autowired
    private PostSubscriberRepository postSubscriberRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PostRepository postRepository;

    private SubscribersPostService subscribersPostService;


    @BeforeAll
    void setUp() {
        subscribersPostService = new SubscribersPostService(postSubscriberRepository, profileRepository);
    }

    @Test
    void getSubscribersPosts_AddedPosts_ReturnsQuantityOfPosts() {
        Profile profile = Profile.builder().username("user").password("p").build();
        profileRepository.save(profile);
        Post post = Post.builder().label("l").content("c").createdAt(LocalDateTime.now()).author(profile).build();
        Post post2 = Post.builder().label("l").content("c").createdAt(LocalDateTime.now()).author(profile).build();
        postRepository.saveAll(List.of(post, post2));
        postSubscriberRepository.save(PostSubscriber.builder().post(post).subscriber(profile).build());
        postSubscriberRepository.save(PostSubscriber.builder().post(post2).subscriber(profile).build());
        Assertions.assertEquals(2, subscribersPostService.getSubscribersPosts("user").size());
    }

    @Test
    void getSubscribersPosts_AddedPosts_ReturnsQuantityOfPostsPageable() {
        Profile profile = Profile.builder().username("user").password("p").build();
        profileRepository.save(profile);
        Post post = Post.builder().label("l").content("c").createdAt(LocalDateTime.now()).author(profile).build();
        Post post2 = Post.builder().label("l").content("c").createdAt(LocalDateTime.now()).author(profile).build();
        postRepository.saveAll(List.of(post, post2));
        postSubscriberRepository.save(PostSubscriber.builder().post(post).subscriber(profile).build());
        postSubscriberRepository.save(PostSubscriber.builder().post(post2).subscriber(profile).build());

        GetPostsPageableDto dto = GetPostsPageableDto.builder().profileUsername("user").page(0).size(5).build();
        List<PostDto> posts = subscribersPostService.getSubscribersPostsPageable(dto);
        Assertions.assertEquals(2, posts.size());
    }

    @Test
    void getSubscribersPosts_ZeroPosts_ReturnsZeroPosts() {
        Profile profile = Profile.builder().username("user").password("p").build();
        profileRepository.save(profile);
        Assertions.assertEquals(0, subscribersPostService.getSubscribersPosts("user").size());
    }


    @Test
    void getSubscribersPosts_ProfileNotExists_ReturnsZeroPosts() {
        Assertions.assertEquals(0, subscribersPostService.getSubscribersPosts("user").size());
    }

}