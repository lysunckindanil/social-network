package org.example.profileservice.function;

import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.service.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Configuration
public class ProfileFunction {
    private final ProfileService profileService;

    @Bean
    public Function<String, ProfileDto> getByUsername() {
        return profileService::getProfileByUsername;
    }

    @Bean
    public Supplier<List<ProfileDto>> getAll() {
        return profileService::getAllProfiles;
    }
}
