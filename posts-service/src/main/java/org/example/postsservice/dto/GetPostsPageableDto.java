package org.example.postsservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetPostsPageableDto {
    @NotEmpty(message = "Username should not be empty")
    String profileUsername;
    @NotNull(message = "Page should not be empty")
    Integer page;
    @NotNull(message = "Size should not be empty")
    Integer size;
}
