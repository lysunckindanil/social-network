package org.example.subscriberpostsservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetPostsPageableDto {
    private String profileUsername;
    private int page;
    private int size;
}
