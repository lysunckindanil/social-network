package org.example.postsservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDto {
    private Long id;
    @NotEmpty(message = "Label should not be empty")
    private String label;
    @NotEmpty(message = "Content should not be empty")
    private String content;
    private LocalDateTime createdAt;
    private String author;
}
