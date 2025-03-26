package org.example.postsservice.service;

import org.example.postsservice.dto.AddPostDto;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.GetPostsPageableDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Post;
import org.example.postsservice.model.Profile;
import org.example.postsservice.repo.PostRepository;
import org.example.postsservice.repo.ProfileRepository;
import org.example.postsservice.util.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private PostsService postsService;

    @BeforeAll
    void setUp() {
        postsService = new PostsService(profileRepository, postRepository);
    }


    @Test
    void createPost_CreatedAtIsNotNull() {
        Post post = new Post();
        post.setLabel("label");
        post.setContent("content");
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        post.setAuthor(profileRepository.save(profile));
        postRepository.save(post);
        Assertions.assertNotNull(postRepository.findById(post.getId()).get().getCreatedAt());
    }

    @Test
    void getPostsByProfileUsernamePageable_ReturnsPagesCorrectly() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        for (int i = 0; i < 10; i++) {
            PostDto postDto = PostDto.builder().label(String.valueOf(i)).content("c").build();
            AddPostDto addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto).build();
            postsService.addPostByUsername(addPostDto);
        }

        GetPostsPageableDto dto = GetPostsPageableDto.builder().page(0).size(7).profileUsername(profile.getUsername()).build();
        List<PostDto> posts = postsService.getPostsByProfileUsernamePageable(dto);
        Assertions.assertEquals(7, posts.size());

        dto = GetPostsPageableDto.builder().page(1).size(7).profileUsername(profile.getUsername()).build();
        posts = postsService.getPostsByProfileUsernamePageable(dto);
        Assertions.assertEquals(3, posts.size());
    }

    @Test
    void getPostsByProfileUsernamePageable_UserDoesntExist_Throws() {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().page(0).size(7).profileUsername("u").build();
        Assertions.assertThrows(BadRequestException.class, () -> postsService.getPostsByProfileUsernamePageable(dto));
    }

    @Test
    void addPostByUsername__UserDoesntExist_Throws() {
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profileUsername("u").post(postDto).build();
        Assertions.assertThrows(BadRequestException.class, () -> postsService.addPostByUsername(addPostDto));
    }

    @Test
    void addPostByUsername_AddPosts_AddsPostToRepository() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);

        Assertions.assertEquals(1, postRepository.findAll().size());
        Assertions.assertEquals(1, postRepository.findByAuthor(profile).size());
    }

    @Test
    void addPostByUsername_AddPostsDiffUsers_AddsPostsToRepository() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);
        Assertions.assertEquals(1, postRepository.findByAuthor(profile).size());

        profile = new Profile();
        profile.setUsername("u1");
        profile.setPassword("p");
        profileRepository.save(profile);
        PostDto postDto2 = PostDto.builder().label("l").content("c").build();
        addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addPostDto);
        Assertions.assertEquals(1, postRepository.findByAuthor(profile).size());

        Assertions.assertEquals(2, postRepository.findAll().size());
    }

    @Test
    void addPostByUsername_AddSomePosts_AddsSomePostToRepository() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        PostDto postDto = PostDto.builder().label("l").content("c").build();
        PostDto postDto2 = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto).build();
        AddPostDto addPostDto2 = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto2).build();
        postsService.addPostByUsername(addPostDto);
        postsService.addPostByUsername(addPostDto2);

        Assertions.assertEquals(2, postRepository.findAll().size());
        Assertions.assertEquals(2, postRepository.findByAuthor(profile).size());
    }

    @Test
    void deletePostByUsername_UserDoesntExist_Throws() {
        Assertions.assertThrows(BadRequestException.class, () -> postsService.deletePost(DeletePostDto.builder().postId(1L).username("u").build()));
    }

    @Test
    void deletePostByUsername_PostDoesntExist_Throws() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);

        Assertions.assertThrows(BadRequestException.class, () -> postsService.deletePost(DeletePostDto.builder().postId(1L).username("u").build()));
    }

    @Test
    void deletePostByUsername_DeletePost_WrongUsername_Throws() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);

        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);
        Long id = postRepository.findAll().getFirst().getId();
        Assertions.assertThrows(BadRequestException.class, () -> postsService.deletePost(DeletePostDto.builder().postId(id).username("u1").build()));

        Assertions.assertEquals(1, postRepository.findByAuthor(profile).size());
    }

    @Test
    void deletePostByUsername_DeletePost_CorrectData_Deletes() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);

        PostDto postDto = PostDto.builder().label("l").content("c").build();
        AddPostDto addPostDto = AddPostDto.builder().profileUsername(profile.getUsername()).post(postDto).build();
        postsService.addPostByUsername(addPostDto);
        Long id = postRepository.findAll().getFirst().getId();
        Assertions.assertDoesNotThrow(() -> postsService.deletePost(DeletePostDto.builder().postId(id).username("u").build()));

        Assertions.assertEquals(0, postRepository.findByAuthor(profile).size());
        Assertions.assertEquals(0, postRepository.count());
    }

}