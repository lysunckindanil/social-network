package org.example.friendpostsservice.service;

import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.repo.PostSubscriberRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
class SubscribersPostServiceTest {
    private PostSubscriberRepository postSubscriberRepository;
    private ProfileRepository profileRepository;
    private SubscribersPostService subscribersPostService;


    @BeforeEach
    void setUp() {
        postSubscriberRepository = Mockito.mock(PostSubscriberRepository.class);
        profileRepository = Mockito.mock(ProfileRepository.class);
        subscribersPostService = new SubscribersPostService(postSubscriberRepository, profileRepository);
    }

    @Test
    void getFriendsPosts_AddedPosts_ReturnsQuantityOfPosts() {
        List<Post> posts = new ArrayList<>();
        Profile profile = new Profile();
        profile.setUsername("user");
        posts.add(Post.builder().author(new Profile()).build());
        posts.add(Post.builder().author(new Profile()).build());
        Mockito.when(profileRepository.findByUsername("user")).thenReturn(Optional.of(profile));
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(2, subscribersPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ZeroPosts_ReturnsZeroPosts() {
        List<Post> posts = new ArrayList<>();
        Profile profile = new Profile();
        profile.setUsername("user");
        Mockito.when(profileRepository.findByUsername("user")).thenReturn(Optional.of(profile));
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(0, subscribersPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileNotExists_ReturnsZeroPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder().author(new Profile()).build());
        posts.add(Post.builder().author(new Profile()).build());
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(0, subscribersPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileDoesntHavePosts_ReturnsZeroPosts() {
        List<Post> posts = new ArrayList<>();
        Profile profile = new Profile();
        profile.setUsername("user");
        Mockito.when(profileRepository.findByUsername("user")).thenReturn(Optional.of(profile));
        Mockito.when(postSubscriberRepository.findPostsBySubscriber(any())).thenReturn(posts);
        Assertions.assertEquals(2, subscribersPostService.getFriendsPosts("user").size());
    }
}