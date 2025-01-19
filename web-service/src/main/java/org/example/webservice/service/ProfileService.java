package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.GetProfilesPageableDto;
import org.example.webservice.dto.ProfileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {
    @Value("${profile.service.page_size}")
    private int pageSize;

    private final ProfileServiceClient profileServiceClient;

    public ProfileDto getProfileByUsername(String username) {
        return profileServiceClient.getByUsername(username);
    }

    public List<ProfileDto> getAllProfiles(int page) {
        GetProfilesPageableDto dto = GetProfilesPageableDto.builder().page(page).size(pageSize).build();
        return profileServiceClient.getAllPageable(dto);
    }

    @FeignClient(name = "profile-service", path = "profile-service")
    interface ProfileServiceClient {
        @PostMapping("/getByUsername")
        ProfileDto getByUsername(@RequestBody String username);

        @PostMapping("/getAllPageable")
        List<ProfileDto> getAllPageable(@RequestBody GetProfilesPageableDto dto);
    }
}
