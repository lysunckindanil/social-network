package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.DeletePostDto;
import org.example.webservice.dto.PostDto;
import org.example.webservice.service.PostsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("posts")
@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;

    @PostMapping("/add")
    public String addPost(@ModelAttribute PostDto post, Principal principal) {
        postsService.addPost(principal.getName(), post);
        return "redirect:/profile/" + principal.getName();
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("postId") Long postId, Principal principal) {
        postsService.deletePost(DeletePostDto.builder().postId(postId).build());
        return "redirect:/profile/" + principal.getName();
    }

    @PostMapping("/getPosts")
    public List<PostDto> getPostsPageable(@RequestParam("page") int page, @RequestParam("username") String username) {
        return postsService.getPostsPageable(username, page);
    }

    @PostMapping("/getSubscriberPosts")
    public List<PostDto> getSubscriberPostsPageable(@RequestParam("page") int page, @RequestParam("username") String username) {
        return postsService.getSubscriberPostsPageable(username, page);
    }


}
