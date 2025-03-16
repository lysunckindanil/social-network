package org.example.webservice.service;

import org.example.webservice.dto.posts.PostDto;

import java.util.List;

public interface PostsService {
    List<PostDto> getPostsPageable(String username, int page);

    List<PostDto> getSubscriberPostsPageable(String username, int page);

    void addPost(String username, PostDto post);

    void deletePost(long postId, String username);
}
