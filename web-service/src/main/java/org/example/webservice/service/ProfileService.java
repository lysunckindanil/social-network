package org.example.webservice.service;

import org.example.webservice.dto.profiles.ProfileDto;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    List<ProfileDto> getProfilesPageable(int page);

    Optional<ProfileDto> getProfileByUsername(String username);
}
