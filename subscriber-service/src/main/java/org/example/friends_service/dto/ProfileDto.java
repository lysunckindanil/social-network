package org.example.friends_service.dto;


import lombok.Builder;

@Builder
public class ProfileDto {
    private String username;
    private String email;
    private String photoUrl;
}
