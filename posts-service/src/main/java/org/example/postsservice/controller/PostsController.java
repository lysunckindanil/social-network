package org.example.postsservice.controller;

import jakarta.validation.*;
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
import java.util.Set;

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
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<PostDto>> violations = validator.validate(dto.getPost());
            if (!violations.isEmpty()) {
                if (violations.size() == 2)
                    throw new BadRequestException("Label and content should not be empty");
                throw new BadRequestException(violations.stream().findAny().get().getMessage());
            }
        } catch (ValidationException e) {
            throw new BadRequestException(e.getMessage());
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
