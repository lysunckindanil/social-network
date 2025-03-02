package org.example.webservice.dto.posts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddPostDto {
    private String profileUsername;
    private PostDto post;
}
