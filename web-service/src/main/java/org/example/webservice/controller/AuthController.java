package org.example.webservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.webservice.model.Profile;
import org.example.webservice.service.security.AuthenticationService;
import org.example.webservice.util.ProfileValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final ProfileValidator profileValidator;

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
        try {
            authenticationService.createProfile(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }
}
