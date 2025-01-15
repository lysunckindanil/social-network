package org.example.webservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.webservice.config.ProfileValidator;
import org.example.webservice.dto.PostDto;
import org.example.webservice.dto.ProfileDto;
import org.example.webservice.model.Profile;
import org.example.webservice.service.FriendsService;
import org.example.webservice.service.PostsService;
import org.example.webservice.service.ProfileSecurityService;
import org.example.webservice.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/profile")
@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileSecurityService profileSecurityService;
    private final FriendsService friendsService;
    private final PostsService postsService;
    private final ProfileValidator profileValidator;

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Principal principal, Model model) {
        ProfileDto profileDto = profileService.getProfileByUsername(username);
        List<PostDto> posts = postsService.getPosts(username);
        if (profileDto != null) {
            model.addAttribute("profile", profileDto);
            model.addAttribute("posts", posts);
            if (principal.getName().equals(username)) {
                model.addAttribute("new_post", PostDto.builder().build());
                return "profile/my_profile";
            }
            model.addAttribute("subscribed", friendsService.getSubscribed(username));
            model.addAttribute("subscribing", friendsService.getSubscribing(username));
            if (friendsService.isISubscribedOn(principal.getName(), username)) {
                return "profile/profile_friend";
            } else {
                return "profile/profile_nofriend";
            }
        }
        return "redirect:/";
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

    @PostMapping("/register")
    public String createUserPost(@ModelAttribute @Valid Profile profile, BindingResult bindingResult) {
        profileValidator.validate(profile, bindingResult);
        if (bindingResult.hasErrors()) {
            return "security/register";
        }
        profileSecurityService.createProfile(profile);
        return "redirect:/";
    }
}
