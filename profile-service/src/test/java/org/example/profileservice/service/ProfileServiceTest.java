package org.example.profileservice.service;

import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.model.Profile;
import org.example.profileservice.repo.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProfileServiceTest {
    @Autowired
    private ProfileRepository profileRepository;
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(profileRepository);
    }


    @Test
    void getProfileByUsername_ReturnsProfile() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        ProfileDto profileDto = profileService.getProfileByUsername(profile.getUsername());
        assertEquals(profileDto.getUsername(), profile.getUsername());
    }

    @Test
    void getAllProfiles_AddProfiles_ReturnsAllProfiles() {
        Profile profile = Profile.builder().username("u").password("p").build();
        profileRepository.save(profile);
        profile = Profile.builder().username("u1").password("p").build();
        profileRepository.save(profile);
        assertEquals(2, profileService.getAllProfiles().size());
        assertEquals("u", profileService.getAllProfiles().getFirst().getUsername());
        assertEquals("u1", profileService.getAllProfiles().getLast().getUsername());
    }
}