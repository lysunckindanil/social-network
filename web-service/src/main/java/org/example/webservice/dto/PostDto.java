package org.example.webservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostDto {
    private String label;
    private String content;
    private Date createdAt;
}
