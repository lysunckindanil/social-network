package org.example.profileservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.GetProfilesPageableDto;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.service.ProfileService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/getByUsername")
    ProfileDto getByUsername(@RequestParam(value = "username", required = false)
                             @NotEmpty(message = "Username should not be empty")
                             String username) {
        return profileService.getProfileByUsername(username);
    }

    @PostMapping("/getAllPageable")
    List<ProfileDto> getAllPageable(@RequestBody @Valid GetProfilesPageableDto dto) {
        return profileService.getAllProfilesPageable(dto);
    }
}
