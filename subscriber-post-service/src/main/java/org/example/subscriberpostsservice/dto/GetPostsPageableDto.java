package org.example.subscriberpostsservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetPostsPageableDto {
    private String profile_username;
    private int page;
    private int size;
}
