package org.example.webservice.service.posts;

import org.example.webservice.dto.posts.PostDto;

import java.util.List;

public interface SubscriberPostService {
    List<PostDto> getSubscriberPostsPageable(String username, int page);
}
