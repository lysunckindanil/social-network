package org.example.postsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.postsservice.dto.AddPostDto;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.GetPostsPageableDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.service.PostsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PostsController.class)
class PostsControllerTest {

    @MockitoBean
    private PostsService postsService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void getPostsPageable_EmptyDto_BadRequest() throws Exception {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().build();
        mvc.perform(
                post("/getPostsPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getPostsPageable_AnyFieldEmptyDto_BadRequest() throws Exception {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().build();
        dto.setProfileUsername("username");
        mvc.perform(
                post("/getPostsPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        dto.setPage(1);
        mvc.perform(
                post("/getPostsPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        dto.setProfileUsername(null);
        dto.setSize(1);
        mvc.perform(
                post("/getPostsPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void addPost_EmptyDto_BadRequest() throws Exception {
        AddPostDto dto = AddPostDto.builder().build();
        mvc.perform(
                post("/addPost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void addPost_EmptyPostDto_BadRequest() throws Exception {
        AddPostDto dto = AddPostDto.builder().build();
        dto.setProfileUsername("username");
        mvc.perform(
                post("/addPost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void addPost_EmptyFieldInPostDto_BadRequest() throws Exception {
        AddPostDto dto = AddPostDto.builder().build();
        dto.setProfileUsername("username");
        dto.setPost(PostDto.builder().content("content").build());
        dto.setProfileUsername("username");
        mvc.perform(
                post("/addPost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void deletePost_EmptyDto_BadRequest() throws Exception {
        DeletePostDto dto = DeletePostDto.builder().build();
        mvc.perform(
                post("/deletePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deletePost_EmptyAnyField_BadRequest() throws Exception {
        DeletePostDto dto = DeletePostDto.builder().build();
        dto.setUsername("username");
        mvc.perform(
                post("/deletePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        dto.setUsername(null);
        dto.setPostId(1L);
        mvc.perform(
                post("/deletePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}