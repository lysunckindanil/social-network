package org.example.subscriberpostsservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetPostsPageableDto {
    @NotEmpty(message = "Username should not be empty")
    private String profileUsername;
    @NotNull(message = "Page should not be empty")
    private Integer page;
    @NotNull(message = "Size should not be empty")
    private Integer size;
}
