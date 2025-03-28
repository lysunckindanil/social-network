package org.example.postsservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddPostDto {
    @NotEmpty(message = "Username should not be empty")
    private String profileUsername;
    @Valid
    @NotNull(message = "Post should exist")
    private PostDto post;
}
