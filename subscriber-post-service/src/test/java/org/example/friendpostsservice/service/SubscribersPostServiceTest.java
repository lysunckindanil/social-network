package org.example.friendpostsservice.service;

import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.model.ProfilePostSubscriber;
import org.example.friendpostsservice.repo.ProfilePostSubscribedRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
class SubscribersPostServiceTest {
    private ProfilePostSubscribedRepository profilePostSubscribedRepository;
    private ProfileRepository profileRepository;
    private SubscribersPostService subscribersPostService;


    @BeforeEach
    void setUp() {
        profilePostSubscribedRepository = Mockito.mock(ProfilePostSubscribedRepository.class);
        profileRepository = Mockito.mock(ProfileRepository.class);
        subscribersPostService = new SubscribersPostService(profilePostSubscribedRepository, profileRepository);
    }

    @Test
    void getFriendsPosts_AddedPosts_ReturnsQuantityOfPosts() {
        ProfilePostSubscriber posts = new ProfilePostSubscriber();
        posts.addPost(Post.builder().author(new Profile()).build());
        posts.addPost(Post.builder().author(new Profile()).build());
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profilePostSubscribedRepository.findProfilePostSubscribedByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(2, subscribersPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ZeroPosts_ReturnsZeroPosts() {
        ProfilePostSubscriber posts = new ProfilePostSubscriber();
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profilePostSubscribedRepository.findProfilePostSubscribedByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(0, subscribersPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileNotExists_ReturnsZeroPosts() {
        ProfilePostSubscriber posts = new ProfilePostSubscriber();
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(profilePostSubscribedRepository.findProfilePostSubscribedByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(0, subscribersPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileDoesntHavePosts_ReturnsZeroPosts() {
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profilePostSubscribedRepository.findProfilePostSubscribedByProfile(any())).thenReturn(Optional.empty());
        Assertions.assertEquals(0, subscribersPostService.getFriendsPosts("user").size());
    }
}