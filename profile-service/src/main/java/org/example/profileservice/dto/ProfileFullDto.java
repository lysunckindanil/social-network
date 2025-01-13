package org.example.profileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * DTO for {@link org.example.profileservice.model.Profile}
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ProfileFullDto implements Serializable {
    private final String username;
    private final String password;
    private final String email;
    private final String photoUrl;
}