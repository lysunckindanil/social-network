package org.example.webservice.service.posts;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.posts.GetPostsPageableDto;
import org.example.webservice.dto.posts.PostDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscriberPostServiceImpl implements SubscriberPostService {
    private final SubscriberPostServiceClient subscriberPostServiceClient;
    @Value("${posts.service.page_size}")
    private int pageSize;

    @Override
    public List<PostDto> getSubscriberPostsPageable(String username, int page) {
        GetPostsPageableDto dto = GetPostsPageableDto.builder().profileUsername(username).page(page).size(pageSize).build();
        return subscriberPostServiceClient.getPosts(dto);
    }

    @FeignClient(name = "subscriber-post-service", path = "subscriber-post-service")
    interface SubscriberPostServiceClient {
        @PostMapping("/getByUsernamePageable")
        List<PostDto> getPosts(@RequestBody GetPostsPageableDto dto);
    }
}
