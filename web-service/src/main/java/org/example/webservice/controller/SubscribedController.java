package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.ProfileDto;
import org.example.webservice.service.SubscriberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("subscribed")
@RequiredArgsConstructor
@Controller
public class SubscribedController {
    private final SubscriberService subscriberService;

    @GetMapping
    public String subscribed(Principal principal, Model model) {
        List<String> subscribed = subscriberService.findSubscribers(principal.getName()).stream().map(ProfileDto::getUsername).toList();
        model.addAttribute("username", principal.getName());
        model.addAttribute("subscribed", subscribed);
        return "friends/subscribed";
    }
}
