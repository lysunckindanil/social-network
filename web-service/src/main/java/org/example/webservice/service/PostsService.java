package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.AddPostDto;
import org.example.webservice.dto.DeletePostDto;
import org.example.webservice.dto.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsServiceClient postsServiceClient;

    public List<PostDto> getPosts(String username) {
        return postsServiceClient.getPosts(username);
    }

    public void addPost(String username, PostDto post) {
        AddPostDto dto = AddPostDto.builder()
                .profile_username(username)
                .post(post)
                .build();
        postsServiceClient.addPost(dto);
    }


    public void deletePost(DeletePostDto post) {

        postsServiceClient.deletePost(post);
    }

    @FeignClient(name = "posts-service", path = "posts-service")
    interface PostsServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/getPosts")
        List<PostDto> getPosts(@RequestBody String username);

        @RequestMapping(method = RequestMethod.POST, value = "/addPost")
        void addPost(@RequestBody AddPostDto post);

        @RequestMapping(method = RequestMethod.POST, value = "/deletePost")
        void deletePost(@RequestBody DeletePostDto post);
    }
}
