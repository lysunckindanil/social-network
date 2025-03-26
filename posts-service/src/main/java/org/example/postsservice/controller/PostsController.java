package org.example.postsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.AddPostDto;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.GetPostsPageableDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.service.PostsService;
import org.example.postsservice.util.BadRequestException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;

    @PostMapping("/getPostsPageable")
    public List<PostDto> getPostsPageable(@RequestBody @Valid GetPostsPageableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return postsService.getPostsByProfileUsernamePageable(dto);
    }

    @PostMapping("/addPost")
    public ResponseEntity<Void> addPost(@RequestBody @Valid AddPostDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }

        postsService.addPostByUsername(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deletePost")
    public ResponseEntity<Void> deletePost(@RequestBody @Valid DeletePostDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        postsService.deletePost(dto);
        return ResponseEntity.noContent().build();
    }
}
