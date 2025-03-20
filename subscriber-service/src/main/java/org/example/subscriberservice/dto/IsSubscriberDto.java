package org.example.subscriberservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IsSubscriberDto {
    @NotEmpty(message = "Profile username should not be empty")
    private String profileUsername;
    @NotEmpty(message = "Subscriber username should not be empty")
    private String subscriberUsername;
}
