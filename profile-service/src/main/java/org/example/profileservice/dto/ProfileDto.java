package org.example.profileservice.dto;

import lombok.Builder;
import lombok.Data;
import org.example.profileservice.model.Profile;

/**
 * DTO for {@link Profile}
 */
@Builder
@Data
public class ProfileDto{
    private final String username;
    private final String email;
    private final String photoUrl;
}