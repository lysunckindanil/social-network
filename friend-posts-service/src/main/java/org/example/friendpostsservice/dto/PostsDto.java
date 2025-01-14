package org.example.friendpostsservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostsDto {
    private List<PostDto> posts;
}
