package org.example.postsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteFromSubscribersDto {
    private Long profile_id;
    private Long post_id;
}
