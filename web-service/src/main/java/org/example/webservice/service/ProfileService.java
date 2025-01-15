package org.example.webservice.service;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.ProfileDto;
import org.example.webservice.model.Profile;
import org.example.webservice.repo.ProfileRepository;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@EnableFeignClients
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileServiceClient profileServiceClient;

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public ProfileDto getProfileByUsername(String username) {
        return profileServiceClient.getByUsername(username);
    }

    @FeignClient(name = "profile-service")
    interface ProfileServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "/getByUsername")
        ProfileDto getByUsername(@RequestBody String username);
    }
}
