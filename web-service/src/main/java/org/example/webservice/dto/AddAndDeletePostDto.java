package org.example.webservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAndDeletePostDto {
    private String profile_username;
    private PostDto post;
}
