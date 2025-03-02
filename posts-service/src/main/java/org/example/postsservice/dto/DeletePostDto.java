package org.example.postsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletePostDto {
    private String username;
    private Long postId;
}
