package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.DeletePostDto;
import org.example.webservice.dto.PostDto;
import org.example.webservice.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        postsService.deletePost(DeletePostDto.builder().post_id(post_id).build());
        return "redirect:/profile/" + principal.getName();
    }

    @ResponseBody
    @PostMapping("/getPosts")
    public List<PostDto> getPostsPageable(@RequestParam("page") int page, Principal principal) {
        return postsService.getPostsPageable(principal.getName(), page);
    }


}
