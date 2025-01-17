package org.example.subscriberservice.dto;


import lombok.Builder;

@Builder
public class ProfileDto {
    private String username;
    private String email;
    private String photoUrl;
}
