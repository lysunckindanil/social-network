package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.posts.PostDto;
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

    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        return principal.getName();
    }

    @GetMapping
    public String index() {
        return "posts/index";
    }

    @GetMapping("/create")
    public String createPostPage(Model model) {
        model.addAttribute("new_post", PostDto.builder().build());
        return "posts/create";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute PostDto post, Principal principal, Model model) {
        if (post.getLabel() == null || post.getLabel().isEmpty() || post.getContent() == null || post.getContent().isEmpty()) {
            model.addAttribute("new_post", post);
            model.addAttribute("error", "Label and content should not be empty");
            return "posts/create";
        }
        postsService.addPost(principal.getName(), post);
        return "redirect:/posts";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("postId") Long postId, Principal principal) {
        postsService.deletePost(postId, principal.getName());
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
