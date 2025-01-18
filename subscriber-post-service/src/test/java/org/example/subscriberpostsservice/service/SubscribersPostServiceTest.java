package org.example.subscriberpostsservice.service;

import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.Profile;
import org.example.subscriberpostsservice.repo.PostSubscriberRepository;
import org.example.subscriberpostsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubscribersPostServiceTest {
    private PostSubscriberRepository postSubscriberRepository;
    private ProfileRepository profileRepository;
    private SubscribersPostService subscribersPostService;


    @BeforeAll
    void setUp() {
        postSubscriberRepository = Mockito.mock(PostSubscriberRepository.class);
        profileRepository = Mockito.mock(ProfileRepository.class);
        subscribersPostService = new SubscribersPostService(postSubscriberRepository, profileRepository);
    }

    @Test
    void getSubscribersPosts_AddedPosts_ReturnsQuantityOfPosts() {
        List<Post> posts = new ArrayList<>();
        Profile profile = new Profile();
        profile.setUsername("user");
        posts.add(Post.builder().author(new Profile()).build());
        posts.add(Post.builder().author(new Profile()).build());
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(profile));
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(profile)).thenReturn(posts);
        Assertions.assertEquals(2, subscribersPostService.getSubscribersPosts("user").size());
    }

    @Test
    void getSubscribersPosts_ZeroPosts_ReturnsZeroPosts() {
        List<Post> posts = new ArrayList<>();
        Profile profile = new Profile();
        profile.setUsername("user");
        Mockito.when(profileRepository.findByUsername("user")).thenReturn(Optional.of(profile));
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(0, subscribersPostService.getSubscribersPosts("user").size());
    }

    @Test
    void getSubscribersPosts_ProfileNotExists_ReturnsZeroPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder().author(new Profile()).build());
        posts.add(Post.builder().author(new Profile()).build());
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(0, subscribersPostService.getSubscribersPosts("user").size());
    }

    @Test
    void getSubscribersPosts_ProfileDoesntHavePosts_ReturnsZeroPosts() {
        List<Post> posts = new ArrayList<>();
        Profile profile = new Profile();
        profile.setUsername("user");
        Mockito.when(profileRepository.findByUsername("user")).thenReturn(Optional.of(profile));
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(0, subscribersPostService.getSubscribersPosts("user").size());
    }
}