package org.example.friendpostsservice.service;

import org.example.friendpostsservice.model.FriendPost;
import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.repo.FriendPostRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
class FriendPostServiceTest {
    private FriendPostRepository friendPostRepository;
    private ProfileRepository profileRepository;
    private FriendPostService friendPostService;


    @BeforeEach
    void setUp() {
        friendPostRepository = Mockito.mock(FriendPostRepository.class);
        profileRepository = Mockito.mock(ProfileRepository.class);
        friendPostService = new FriendPostService(friendPostRepository, profileRepository);
    }

    @Test
    void getFriendsPosts_AddedPosts_ReturnsQuantityOfPosts() {
        FriendPost posts = new FriendPost();
        posts.addPost(new Post());
        posts.addPost(new Post());
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(friendPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(2, friendPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ZeroPosts_ReturnsZeroPosts() {
        FriendPost posts = new FriendPost();
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(friendPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(0, friendPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileNotExists_ReturnsZeroPosts() {
        FriendPost posts = new FriendPost();
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(friendPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.of(posts));
        Assertions.assertEquals(0, friendPostService.getFriendsPosts("user").size());
    }

    @Test
    void getFriendsPosts_ProfileDoesntHavePosts_ReturnsZeroPosts() {
        Mockito.when(profileRepository.findByUsername(any())).thenReturn(Optional.of(new Profile()));
        Mockito.when(friendPostRepository.findFriendPostByProfile(any())).thenReturn(Optional.empty());
        Assertions.assertEquals(0, friendPostService.getFriendsPosts("user").size());
    }
}