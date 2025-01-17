package org.example.sharepostsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletePostDto {
    private Long post_id;
}
