package org.example.postsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.AddPostDto;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.GetPostsPageableDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.service.PostsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;

    @PostMapping("/getPostsPageable")
    public List<PostDto> getPostsPageable(@RequestBody @Valid GetPostsPageableDto dto) {
        return postsService.getPostsByProfileUsernamePageable(dto);
    }

    @PostMapping("/addPost")
    public ResponseEntity<Void> addPost(@RequestBody @Valid AddPostDto dto) {
        postsService.addPostByUsername(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deletePost")
    public ResponseEntity<Void> deletePost(@RequestBody @Valid DeletePostDto dto) {
        postsService.deletePost(dto);
        return ResponseEntity.noContent().build();
    }
}
