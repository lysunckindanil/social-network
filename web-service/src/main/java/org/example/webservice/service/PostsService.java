package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
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

    @FeignClient(name = "posts-service", url = "http://192.168.0.100:8000", path = "posts-service")
    interface PostsServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/getPosts")
        List<PostDto> getPosts(@RequestBody String username);
    }
}
