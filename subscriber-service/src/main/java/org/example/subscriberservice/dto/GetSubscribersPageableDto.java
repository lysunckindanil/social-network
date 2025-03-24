package org.example.subscriberservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSubscribersPageableDto {
    @NotEmpty(message = "Profile username should not be empty")
    private String profileUsername;
    @NotNull(message = "Number of page should not be empty")
    private Integer page;
    @NotNull(message = "Size of page should not be empty")
    private Integer size;
}
