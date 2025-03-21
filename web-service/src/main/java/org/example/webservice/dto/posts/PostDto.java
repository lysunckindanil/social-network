package org.example.webservice.dto.posts;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDto {
    private Long id;
    private String label;
    private String content;
    private LocalDateTime createdAt;
    private String author;
}
