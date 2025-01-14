package org.example.friendpostsservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PostDto {
    private String label;
    private String content;
    private Date createdAt;
}
