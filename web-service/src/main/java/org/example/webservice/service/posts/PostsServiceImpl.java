package org.example.webservice.service.posts;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.posts.AddPostDto;
import org.example.webservice.dto.posts.DeletePostDto;
import org.example.webservice.dto.posts.GetPostsPageableDto;
import org.example.webservice.dto.posts.PostDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService {
    private final PostsServiceClient postsServiceClient;
    private final SubscriberPostServiceClient subscriberPostServiceClient;
    @Value("${posts.service.page_size}")
    private int pageSize;

    @Override
    public List<PostDto> getPostsPageable(String username, int page) {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().profileUsername(username).page(page).size(pageSize).build();
        return postsServiceClient.getPosts(dto);
    }

    @Override
    public List<PostDto> getSubscriberPostsPageable(String username, int page) {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().profileUsername(username).page(page).size(pageSize).build();
        return subscriberPostServiceClient.getPosts(dto);
    }

    @Override
    public void addPost(String username, PostDto post) {
        AddPostDto dto = AddPostDto.builder()
                .profileUsername(username)
                .post(post)
                .build();
        postsServiceClient.addPost(dto);
    }

    @Override
    public void deletePost(long postId, String username) {
        postsServiceClient.deletePost(DeletePostDto.builder()
                .postId(postId)
                .username(username)
                .build());
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
