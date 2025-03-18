package org.example.postsservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletePostDto {
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @NotNull(message = "PostId should not be empty")
    private Long postId;
}
