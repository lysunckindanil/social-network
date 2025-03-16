package org.example.webservice.service.profiles;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.profiles.GetProfilesPageableDto;
import org.example.webservice.dto.profiles.ProfileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    @Value("${profile.service.page_size}")
    private int pageSize;

    private final ProfileServiceClient profileServiceClient;

    @Override
    public List<ProfileDto> getProfilesPageable(int page) {
        GetProfilesPageableDto dto = GetProfilesPageableDto.builder().page(page).size(pageSize).build();
        return profileServiceClient.getAllPageable(dto);
    }

    @Override
    public Optional<ProfileDto> getProfileByUsername(String username) {
        return Optional.ofNullable(profileServiceClient.getByUsername(username));
    }

    @FeignClient(name = "profile-service", path = "profile-service")
    interface ProfileServiceClient {
        @PostMapping("/getByUsername")
        ProfileDto getByUsername(@RequestBody String username);

        @PostMapping("/getAllPageable")
        List<ProfileDto> getAllPageable(@RequestBody GetProfilesPageableDto dto);
    }
}
