package org.example.subscriberservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IsSubscriberDto {
    private String profileUsername;
    private String subscriberUsername;
}
