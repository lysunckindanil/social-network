package org.example.subscriberpostsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.service.SubscribersPostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class SubscriberPostController {
    private final SubscribersPostService subscribersPostService;

    @PostMapping("/getByUsernamePageable")
    public List<PostDto> getByUsernamePageable(@Valid @RequestBody GetPostsPageableDto dto) {
        return subscribersPostService.getSubscribersPostsPageable(dto);
    }
}
