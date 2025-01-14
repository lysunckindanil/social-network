package org.example.postsservice.function;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.AddAndDeletePostDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.service.PostsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class PostsFunction {
    private final PostsService postsService;

    @Bean
    public Function<String, List<PostDto>> getPosts() {
        return postsService::getPostsByProfileUsername;
    }

    @Bean
    public Consumer<AddAndDeletePostDto> addPost() {
        return postsService::addPostByUsername;
    }


    @Bean
    public Consumer<AddAndDeletePostDto> deletePost() {
        return postsService::deletePostByUsername;
    }
}
