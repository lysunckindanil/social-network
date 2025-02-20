package org.example.webservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webservice.dto.PostDto;
import org.example.webservice.dto.ProfileDto;
import org.example.webservice.model.Profile;
import org.example.webservice.service.ProfileService;
import org.example.webservice.service.SubscriberService;
import org.example.webservice.service.security.CookieService;
import org.example.webservice.service.security.ProfileSecurityService;
import org.example.webservice.util.ProfileValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequestMapping("/profile")
@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileSecurityService profileSecurityService;
    private final SubscriberService subscriberService;
    private final ProfileValidator profileValidator;
    private final CookieService cookieService;

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        ProfileDto profileDto = profileService.getProfileByUsername(username);
        if (profileDto != null) {
            model.addAttribute("profile", profileDto);
            if (principal.getName().equals(username)) {
                model.addAttribute("new_post", PostDto.builder().build());
                return "profile/my_profile";
            }
            model.addAttribute("subscribed", subscriberService.findSubscribers(username));
            model.addAttribute("subscribing", subscriberService.findProfileSubscribedOn(username));
            if (subscriberService.isISubscribedOn(principal.getName(), username)) {
                return "profile/profile_subscribed";
            } else {
                return "profile/profile_not_subscribed";
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
        return profileService.getAllProfiles(page);
    }

    @GetMapping
    public String index(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "profile/index";
    }

    @PostMapping("/register")
    public String createUserPost(@ModelAttribute @Valid Profile profile, BindingResult bindingResult, HttpServletResponse response) {
        profileValidator.validate(profile, bindingResult);
        if (bindingResult.hasErrors()) {
            return "security/register";
        }
        try {
            profileSecurityService.createProfile(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Cookie cookie = cookieService.buildCookie(profile.getUsername());
        response.addCookie(cookie);
        return "redirect:/";
    }
}
