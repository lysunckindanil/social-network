package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final SubscriberPostServiceClient subscriberPostServiceClient;

    public List<PostDto> getFriendsPostsByUsername(String username) {
        return subscriberPostServiceClient.getPostsByUsername(username);
    }


    @FeignClient(name = "subscriber-post-service", path = "subscriber-post-service")
    interface SubscriberPostServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/getByUsername")
        List<PostDto> getPostsByUsername(@RequestBody String username);
    }
}
