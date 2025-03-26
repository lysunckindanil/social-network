package org.example.subscriberpostsservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.service.SubscribersPostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class SubscriberPostController {
    private final SubscribersPostService subscribersPostService;

    @PostMapping("/getByUsername")
    public List<PostDto> getByUsername(@RequestParam(value = "username", required = false)
                                       @NotEmpty(message = "Username should not be empty") String username) {
        return subscribersPostService.getSubscribersPosts(username);
    }

    @PostMapping("/getByUsernamePageable")
    public List<PostDto> getByUsernamePageable(@Valid @RequestBody GetPostsPageableDto dto) {
        return subscribersPostService.getSubscribersPostsPageable(dto);
    }
}
