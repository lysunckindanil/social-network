package org.example.webservice.dto.profiles;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileDto {
    private final String username;
    private final String email;
    private final String photoUrl;
}