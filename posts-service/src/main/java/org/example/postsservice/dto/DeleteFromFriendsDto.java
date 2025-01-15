package org.example.postsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteFromFriendsDto {
    private Long profile_id;
    private Long post_id;
}
