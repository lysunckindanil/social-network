package org.example.profileservice.dto;

import lombok.*;
import org.example.profileservice.model.Profile;

import java.io.Serializable;

/**
 * DTO for {@link Profile}
 */
@Builder
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ProfileDto implements Serializable {
    private final String username;
    private final String email;
    private final String photoUrl;
}