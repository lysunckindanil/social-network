package org.example.subscriberservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDto {
    private String username;
    private String email;
    private String photoUrl;
}
