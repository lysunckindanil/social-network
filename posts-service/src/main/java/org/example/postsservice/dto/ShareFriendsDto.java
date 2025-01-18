package org.example.postsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShareFriendsDto {
    private Long profileId;
    private Long postId;
}
