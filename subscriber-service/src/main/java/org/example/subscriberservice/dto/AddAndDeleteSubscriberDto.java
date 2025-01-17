package org.example.subscriberservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAndDeleteSubscriberDto {
    private String profile_username;
    private String subscriber_username;
}
