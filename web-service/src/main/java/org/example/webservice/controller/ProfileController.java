package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{username}")
    public String profile(@PathVariable String username) {
        return profileService.getProfileByUsername(username).toString();
    }
}
