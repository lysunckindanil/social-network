package org.example.profileservice.service;

import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.model.Profile;
import org.example.profileservice.repo.ProfileRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProfileServiceTest {
    @Autowired
    private ProfileRepository profileRepository;
    private ProfileService profileService;

    @BeforeAll
    void setUp() {
        profileService = new ProfileService(profileRepository);
    }


    @Test
    void getProfileByUsername_ReturnsProfile() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        ProfileDto profileDto = profileService.getProfileByUsername(profile.getUsername());
        assertEquals(profileDto.getUsername(), profile.getUsername());
    }

    @Test
    void getAllProfiles_AddProfiles_ReturnsAllProfiles() {
        Profile profile = new Profile();
        profile.setUsername("u");
        profile.setPassword("p");
        profileRepository.save(profile);
        profile = new Profile();
        profile.setUsername("u1");
        profile.setPassword("p");
        profileRepository.save(profile);
        assertEquals(2, profileService.getAllProfiles().size());
        assertEquals("u", profileService.getAllProfiles().getFirst().getUsername());
        assertEquals("u1", profileService.getAllProfiles().getLast().getUsername());
    }
}