package org.example.profileservice.function;

import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.model.Profile;
import org.example.profileservice.service.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class ProfileFunction {
    private final ProfileService profileService;

    @Bean
    public Function<String, ProfileDto> getByEmail() {
        return profileService::getProfileByEmail;
    }

    @Bean
    public Function<String, ProfileDto> getByUsername() {
        return profileService::getProfileByUsername;
    }

    @Bean
    public Consumer<Profile> save() {
        return profileService::saveProfile;
    }

    @Bean
    public Consumer<String> deleteByUsername() {
        return profileService::deleteProfile;
    }
}
