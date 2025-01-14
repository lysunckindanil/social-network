package org.example.postsservice.service;

import org.example.postsservice.dto.AddAndDeletePostDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Profile;
import org.example.postsservice.repo.PostRepository;
import org.example.postsservice.repo.ProfilePostRepository;
import org.example.postsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PostsServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfilePostRepository profilePostRepository;

    private PostsService postsService;
    @BeforeEach
    void setUp() {
        postsService = new PostsService(profileRepository, postRepository, profilePostRepository);
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
        Assertions.assertEquals(1, profilePostRepository.findAll().getFirst().getPosts().size());
    }

    @Test
    void addPostByUsername_AddPostsDiffUsers_AddsPostsToRepository() {
        Profile profile = Profile.builder().username("username").build();
        Profile profile2 = Profile.builder().username("username2").build();
        profileRepository.save(profile);
        profileRepository.save(profile2);
        Date date = new Date();
        PostDto postDto = PostDto.builder().createdAt(date).build();
        PostDto postDto2 = PostDto.builder().createdAt(date).build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        AddAndDeletePostDto addAndDeletePostDto2 = AddAndDeletePostDto.builder().profile_username(profile2.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        postsService.addPostByUsername(addAndDeletePostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(2, profilePostRepository.findAll().size());
    }

    @Test
    void addPostByUsername_AddSomePosts_AddsSomePostToRepository() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        Date date = new Date();
        PostDto postDto = PostDto.builder().createdAt(date).build();
        PostDto postDto2 = PostDto.builder().createdAt(date).build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        AddAndDeletePostDto addAndDeletePostDto2 = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        postsService.addPostByUsername(addAndDeletePostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(2, profilePostRepository.findAll().getFirst().getPosts().size());
    }

    @Test
    void deletePostByUsername_DeletePost_DeletesPostFromRepository() {
        Profile profile = Profile.builder().username("username").build();
        profileRepository.save(profile);
        Date date = new Date();
        PostDto postDto = PostDto.builder().createdAt(date).build();
        AddAndDeletePostDto addAndDeletePostDto = AddAndDeletePostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addAndDeletePostDto);
        postsService.deletePostByUsername(addAndDeletePostDto);

        Assertions.assertEquals(0, postRepository.findAll().size());
        Assertions.assertEquals(0, profilePostRepository.findAll().getFirst().getPosts().size());
    }
}