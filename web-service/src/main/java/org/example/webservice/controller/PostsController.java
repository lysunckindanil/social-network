package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.PostDto;
import org.example.webservice.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String deletePost(@ModelAttribute PostDto post, Principal principal) {
        postsService.deletePost(principal.getName(), post);
        return "redirect:/profile/" + principal.getName();
    }
}
