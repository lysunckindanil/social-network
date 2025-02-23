package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.DeletePostDto;
import org.example.webservice.dto.PostDto;
import org.example.webservice.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("posts")
@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;

    @GetMapping
    public String index(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "posts/index";
    }

    @GetMapping("/create")
    public String create(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("new_post", PostDto.builder().build());
        return "posts/create";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute PostDto post, Principal principal) {
        postsService.addPost(principal.getName(), post);
        return "redirect:/posts";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("postId") Long postId, Principal principal) {
        postsService.deletePost(DeletePostDto.builder().postId(postId).build());
        return "redirect:/posts";
    }

    @ResponseBody
    @PostMapping("/getPosts")
    public List<PostDto> getPostsPageable(@RequestParam("page") int page, @RequestParam("username") String username) {
        return postsService.getPostsPageable(username, page);
    }

    @ResponseBody
    @PostMapping("/getSubscriberPosts")
    public List<PostDto> getSubscriberPostsPageable(@RequestParam("page") int page, @RequestParam("username") String username) {
        return postsService.getSubscriberPostsPageable(username, page);
    }
}
