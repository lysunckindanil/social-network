package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webservice.dto.profiles.ProfileDto;
import org.example.webservice.service.profiles.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/profile")
@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final ProfileService profileService;

    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        return principal.getName();
    }

    @GetMapping
    public String index() {
        return "profile/index";
    }

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Principal principal, Model model) {
        Optional<ProfileDto> profileOptional = profileService.getProfileByUsername(username);
        if (profileOptional.isPresent()) {
            ProfileDto profile = profileOptional.get();
            model.addAttribute("profile", profile);
            if (principal.getName().equals(username)) {
                return "profile/my_profile";
            } else {
                model.addAttribute("username_cur", username);
                return "profile/profile";
            }
        }
        return "redirect:/home";
    }

    @ResponseBody
    @PostMapping
    public List<ProfileDto> getProfilesPageable(@RequestParam("page") int page) {
        return profileService.getProfilesPageable(page);
    }
}
