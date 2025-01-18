package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.AddPostDto;
import org.example.webservice.dto.DeletePostDto;
import org.example.webservice.dto.GetPostsPageableDto;
import org.example.webservice.dto.PostDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsServiceClient postsServiceClient;
    private final SubscriberPostServiceClient subscriberPostServiceClient;
    @Value("${posts.service.page_size}")
    private int pageSize;

    public List<PostDto> getPostsPageable(String username, int page) {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().profileUsername(username).page(page).size(pageSize).build();
        return postsServiceClient.getPosts(dto);
    }

    public List<PostDto> getSubscriberPostsPageable(String username, int page) {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().profileUsername(username).page(page).size(pageSize).build();
        return subscriberPostServiceClient.getPosts(dto);
    }

    public void addPost(String username, PostDto post) {
        AddPostDto dto = AddPostDto.builder()
                .profileUsername(username)
                .post(post)
                .build();
        postsServiceClient.addPost(dto);
    }


    public void deletePost(DeletePostDto post) {
        postsServiceClient.deletePost(post);
    }


    @FeignClient(name = "subscriber-post-service", path = "subscriber-post-service")
    interface SubscriberPostServiceClient {
        @PostMapping("/getByUsernamePageable")
        List<PostDto> getPosts(@RequestBody GetPostsPageableDto dto);
    }

    @FeignClient(name = "posts-service", path = "posts-service")
    interface PostsServiceClient {
        @PostMapping("/getPosts")
        List<PostDto> getPosts(@RequestBody String username);

        @PostMapping("/getPostsPageable")
        List<PostDto> getPosts(@RequestBody GetPostsPageableDto dto);

        @PostMapping("/addPost")
        void addPost(@RequestBody AddPostDto post);

        @PostMapping("/deletePost")
        void deletePost(@RequestBody DeletePostDto post);
    }
}
