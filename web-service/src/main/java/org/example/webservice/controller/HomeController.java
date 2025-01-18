package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "home/home";
    }

    @GetMapping()
    public String redirect() {
        return "redirect:/home";
    }

}
