package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class HomeController {
    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/home")
    public String home() {
        return "home/home";
    }

    @GetMapping()
    public String redirect() {
        return "redirect:/home";
    }
}
