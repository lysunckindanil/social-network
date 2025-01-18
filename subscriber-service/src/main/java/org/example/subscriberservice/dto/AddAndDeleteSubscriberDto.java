package org.example.subscriberservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAndDeleteSubscriberDto {
    private String profileUsername;
    private String subscriberUsername;
}
