package org.example.subscriberpostsservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetPostsPageableDto {
    @NotEmpty
    private String profileUsername;
    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
}
