package org.example.friendpostsservice.service;

import org.example.friendpostsservice.model.ProfileSubscribedByPost;
import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.repo.ProfileSubscribedByPostRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
class ProfileSubscribedByPostServiceTest {
    private ProfileSubscribedByPostRepository profileSubscribedByPostRepository;
    private ProfileRepository profileRepository;
    private FriendPostService friendPostService;


    @BeforeEach
    void setUp() {
        profileSubscribedByPostRepository = Mockito.mock(ProfileSubscribedByPostRepository.class);
        profileRepository = Mockito.mock(ProfileRepository.class);
        friendPostService = new FriendPostService(profileSubscribedByPostRepository, profileRepository);
    }

    @Test
    void getFriendsPosts_AddedPosts_ReturnsQuantityOfPosts() {
        ProfileSubscribedByPost posts = new ProfileSubscribedByPost();
        posts.addPost(new Post());
        posts.addPost(new Post());
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profileSubscribedByPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(2, friendPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ZeroPosts_ReturnsZeroPosts() {
        ProfileSubscribedByPost posts = new ProfileSubscribedByPost();
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profileSubscribedByPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(0, friendPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileNotExists_ReturnsZeroPosts() {
        ProfileSubscribedByPost posts = new ProfileSubscribedByPost();
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(profileSubscribedByPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(0, friendPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileDoesntHavePosts_ReturnsZeroPosts() {
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profileSubscribedByPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.empty());
        Assertions.assertEquals(0, friendPostService.getFriendsPosts("user").size());
    }
}