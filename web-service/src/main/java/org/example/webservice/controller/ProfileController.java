package org.example.webservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webservice.dto.profiles.ProfileDto;
import org.example.webservice.model.Profile;
import org.example.webservice.service.ProfileService;
import org.example.webservice.service.security.ProfileSecurityService;
import org.example.webservice.util.ProfileValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final ProfileSecurityService profileSecurityService;
    private final ProfileValidator profileValidator;

    @GetMapping
    public String index(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "profile/index";
    }


    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Principal principal, Model model) {
        Optional<Profile> profileOptional = profileSecurityService.getProfileByUsername(username);
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            model.addAttribute("username", principal.getName());
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

    @GetMapping("/login")
    public String loginProfileForm() {
        return "security/login";
    }

    @GetMapping("/register")
    public String registerProfileForm(Model model) {
        model.addAttribute("profile", new Profile());
        return "security/register";
    }

    @ResponseBody
    @PostMapping
    public List<ProfileDto> getProfilesPageable(@RequestParam("page") int page) {
        return profileService.getProfilesPageable(page);
    }

    @PostMapping("/register")
    public String createUserPost(@ModelAttribute @Valid Profile profile, BindingResult bindingResult) {
        profileValidator.validate(profile, bindingResult);
        if (bindingResult.hasErrors()) {
            return "security/register";
        }
        try {
            profileSecurityService.createProfile(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }

}
