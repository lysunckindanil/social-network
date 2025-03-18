package org.example.webservice.controller.posts;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.posts.PostDto;
import org.example.webservice.service.posts.PostsService;
import org.example.webservice.service.posts.SubscriberPostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsRestController {

    private final PostsService postsService;
    private final SubscriberPostService subscriberPostService;

    @PostMapping
    public List<PostDto> getPostsPageable(@RequestParam("page") int page, @RequestParam("username") String username) {
        return postsService.getPostsPageable(username, page);
    }

    @PostMapping("/getSubscriberPosts")
    public List<PostDto> getSubscriberPostsPageable(@RequestParam("page") int page, @RequestParam("username") String username) {
        return subscriberPostService.getSubscriberPostsPageable(username, page);
    }
}
