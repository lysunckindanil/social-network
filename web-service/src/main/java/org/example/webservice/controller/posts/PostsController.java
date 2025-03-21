package org.example.webservice.controller.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.posts.PostDto;
import org.example.webservice.service.posts.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@RequestMapping("posts")
@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;
    private final ObjectMapper objectMapper;

    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        return principal.getName();
    }

    @GetMapping
    public String index() {
        return "posts/index";
    }

    @GetMapping("/post")
    public String createPostPage(Model model) {
        model.addAttribute("new_post", PostDto.builder().build());
        return "posts/create";
    }

    @PostMapping("/post")
    public String addPost(@ModelAttribute PostDto post, Principal principal, Model model) throws JsonProcessingException {
        try {
            postsService.addPost(principal.getName(), post);
        } catch (FeignException e) {
            model.addAttribute("new_post", post);
            model.addAttribute("error", objectMapper.readValue(e.contentUTF8(), HashMap.class).get("error"));
            return "posts/create";
        }
        return "redirect:/posts";
    }

    @DeleteMapping("/post")
    public String deletePost(@RequestParam("postId") Long postId, Principal principal) {
        postsService.deletePost(postId, principal.getName());
        return "redirect:/posts";
    }
}
