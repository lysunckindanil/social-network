package org.example.postsservice.service;

import org.example.postsservice.dto.AddPostDto;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.GetPostsPageableDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Profile;
import org.example.postsservice.repo.PostRepository;
import org.example.postsservice.repo.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @BeforeAll
    void setUp() {
        shareSubscribersClient = Mockito.mock(ShareSubscribersClient.class);
        Mockito.doNothing().when(shareSubscribersClient).shareSubscribers(Mockito.any(), Mockito.any());
        postsService = new PostsService(profileRepository, postRepository, shareSubscribersClient);
    }

    @Test
    void getPostsByProfileUsernamePageable_ReturnsPagesCorrectly() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        for (int i = 0; i < 10; i++) {
            PostDto postDto = PostDto.builder().label(String.valueOf(i)).content("c").build();
            AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
            postsService.addPostByUsername(addPostDto);
        }

        GetPostsPageableDto dto = GetPostsPageableDto.builder().page(0).size(7).profile_username(profile.getUsername()).build();
        List<PostDto> posts = postsService.getPostsByProfileUsernamePageable(dto);
        int counter = 9;
        for (PostDto postDto : posts) {
            Assertions.assertEquals(String.valueOf(counter--), postDto.getLabel());
        }

        dto.setPage(1);
        posts = postsService.getPostsByProfileUsernamePageable(dto);
        for (PostDto postDto : posts) {
            Assertions.assertEquals(String.valueOf(counter--), postDto.getLabel());
        }

        dto.setPage(2);
        posts = postsService.getPostsByProfileUsernamePageable(dto);
        Assertions.assertTrue(posts.isEmpty());

    }

    @Test
    void addPostByUsername_AddPosts_AddsPostToRepository() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);

        Assertions.assertEquals(1, postRepository.findAll().size());
        Assertions.assertEquals(1, postRepository.findAllByAuthor(profile).size());
    }

    @Test
    void addPostByUsername_AddPosts_CallsSharePostsToSubscribers() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);
        Long post_id = postRepository.findAll().getFirst().getId();
        Long profile_id = profile.getId();
        Mockito.verify(shareSubscribersClient).shareSubscribers(profile_id, post_id);
    }

    @Test
    void addPostByUsername_AddPostsDiffUsers_AddsPostsToRepository() {
        Profile profile = Profile.builder().username("u").password("p").build();
        Profile profile2 = Profile.builder().username("u1").password("p").build();
        profileRepository.save(profile);
        profileRepository.save(profile2);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        PostDto postDto2 = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        AddPostDto addPostDto2 = AddPostDto.builder().profile_username(profile2.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addPostDto);
        postsService.addPostByUsername(addPostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(1, postRepository.findAllByAuthor(profile).size());
        Assertions.assertEquals(1, postRepository.findAllByAuthor(profile2).size());
    }

    @Test
    void addPostByUsername_AddSomePosts_AddsSomePostToRepository() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        PostDto postDto2 = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        AddPostDto addPostDto2 = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addPostDto);
        postsService.addPostByUsername(addPostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(2, postRepository.findAllByAuthor(profile).size());
    }

    @Test
    void deletePostByUsername_DeletePost_DeletesFromAuthor() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);
        Long id = postRepository.findAll().getFirst().getId();
        postsService.deletePost(DeletePostDto.builder().post_id(id).build());

        Assertions.assertEquals(0, postRepository.findAllByAuthor(profile).size());
    }

    @Test
    void deletePostByUsername_DeletePost_CallsDeletesFromSubscribers() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profile_username(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);
        Long id = postRepository.findAll().getFirst().getId();
        postsService.deletePost(DeletePostDto.builder().post_id(id).build());

        Mockito.verify(shareSubscribersClient, Mockito.times(1)).deleteFromSubscribers(id);
    }


}