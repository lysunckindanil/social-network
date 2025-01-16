package org.example.postsservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostDto{
    private Long id;
    private String label;
    private String content;
    private Date createdAt;
}
