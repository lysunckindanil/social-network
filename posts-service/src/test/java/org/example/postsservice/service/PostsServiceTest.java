package org.example.postsservice.service;

import org.example.postsservice.dto.AddAndDeletePostDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Profile;
import org.example.postsservice.repo.PostRepository;
import org.example.postsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PostsServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProfileRepository profileRepository;
    private ShareSubscribersClient shareSubscribersClient;

    private PostsService postsService;

    @BeforeEach
    void setUp() {
        shareSubscribersClient = Mockito.mock(ShareSubscribersClient.class);
        Mockito.doNothing().when(shareSubscribersClient).shareSubscribers(Mockito.any(), Mockito.any());
        postsService = new PostsService(profileRepository, postRepository, shareSubscribersClient);
    }


    @Test
    void addPostByUsername_AddPosts_AddsPostToRepository() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        Date date = new Date();
        PostDto postDto = PostDto.builder().createdAt(date).build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addAndDeletePostDto);

        Assertions.assertEquals(1, postRepository.findAll().size());
        Assertions.assertEquals(1, postRepository.findAllByAuthor(profile).size());
    }

    @Test
    void addPostByUsername_AddPosts_CallsSharePostsToSubscribers() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        Date date = new Date();
        PostDto postDto = PostDto.builder().createdAt(date).build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        Long post_id = postRepository.findAll().getFirst().getId();
        Long profile_id = profile.getId();
        Mockito.verify(shareSubscribersClient).shareSubscribers(profile_id, post_id);
    }

    @Test
    void addPostByUsername_AddPostsDiffUsers_AddsPostsToRepository() {
        Profile profile = Profile.builder().username("username").build();
        Profile profile2 = Profile.builder().username("username2").build();
        profileRepository.save(profile);
        profileRepository.save(profile2);
        PostDto postDto = PostDto.builder().build();
        PostDto postDto2 = PostDto.builder().build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        AddAndDeletePostDto addAndDeletePostDto2 = AddAndDeletePostDto.builder().profile_username(profile2.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        postsService.addPostByUsername(addAndDeletePostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(1, postRepository.findAllByAuthor(profile).size());
        Assertions.assertEquals(1, postRepository.findAllByAuthor(profile2).size());
    }

    @Test
    void addPostByUsername_AddSomePosts_AddsSomePostToRepository() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().build();
        PostDto postDto2 = PostDto.builder().build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        AddAndDeletePostDto addAndDeletePostDto2 = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        postsService.addPostByUsername(addAndDeletePostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(2, postRepository.findAllByAuthor(profile).size());
    }

    @Test
    void deletePostByUsername_DeletePost_DeletesFromAuthor() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        Date createdAt = postRepository.findAll().getFirst().getCreatedAt();
        addAndDeletePostDto.getPost().setCreatedAt(createdAt);
        postsService.deletePostByUsername(addAndDeletePostDto);

        Assertions.assertEquals(0, postRepository.findAllByAuthor(profile).size());
    }

    @Test
    void deletePostByUsername_DeletePost_CallsDeletesFromSubscribers() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        Date createdAt = postRepository.findAll().getFirst().getCreatedAt();
        addAndDeletePostDto.getPost().setCreatedAt(createdAt);
        postsService.deletePostByUsername(addAndDeletePostDto);
        Long post_id = postRepository.findAll().getFirst().getId();
        Long profile_id = profile.getId();

        Mockito.verify(shareSubscribersClient, Mockito.times(1)).deleteFromSubscribers(profile_id, post_id);
    }


}