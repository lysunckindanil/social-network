package org.example.postsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddPostDto {
    private String profile_username;
    private PostDto post;
}
