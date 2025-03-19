package org.example.profileservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.profileservice.dto.GetProfilesPageableDto;
import org.example.profileservice.dto.ProfileDto;
import org.example.profileservice.service.BadRequestException;
import org.example.profileservice.service.ProfileService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/getByUsername")
    ProfileDto getByUsername(@RequestBody String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Username cannot be null or empty");
        }
        return profileService.getProfileByUsername(username);
    }

    @PostMapping("/getAllPageable")
    List<ProfileDto> getAllPageable(@RequestBody @Valid GetProfilesPageableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        return profileService.getAllProfilesPageable(dto);
    }
}
