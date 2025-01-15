package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.PostDto;
import org.example.webservice.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequestMapping("posts")
@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;

    @PostMapping("/add")
    public String addPost(@ModelAttribute PostDto post, Principal principal) {
        postsService.addPost(principal.getName(), post);
        return "redirect:/profile/" + principal.getName();
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("post_id") Long post_id, Principal principal) {
        postsService.deletePost(principal.getName(), PostDto.builder().id(post_id).build());
        return "redirect:/profile/" + principal.getName();
    }
}
