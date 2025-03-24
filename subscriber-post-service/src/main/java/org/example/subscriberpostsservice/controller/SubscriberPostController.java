package org.example.subscriberpostsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.service.SubscribersPostService;
import org.example.subscriberpostsservice.util.BadRequestException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class SubscriberPostController {
    private final SubscribersPostService subscribersPostService;

    @PostMapping("/getByUsername")
    public List<PostDto> getByUsername(@RequestBody String username) {
        return subscribersPostService.getSubscribersPosts(username);
    }

    @PostMapping("/getByUsernamePageable")
    public List<PostDto> getByUsernamePageable(@Valid @RequestBody GetPostsPageableDto dto, BindingResult bindingResult) {
        System.out.println(dto.getPage());
        System.out.println(dto.getSize());
        System.out.println(dto.getProfileUsername());
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return subscribersPostService.getSubscribersPostsPageable(dto);
    }
}
