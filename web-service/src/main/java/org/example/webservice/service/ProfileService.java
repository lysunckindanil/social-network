package org.example.webservice.service;

import org.example.webservice.dto.profiles.ProfileDto;

import java.util.List;

public interface ProfileService {
    List<ProfileDto> getProfilesPageable(int page);
}
