package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.service.FriendsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("friends")
@RequiredArgsConstructor
@Controller
public class SubscribedController {
    private final FriendsService friendsService;

    @GetMapping
    public String subscribed(Principal principal, Model model) {
        List<String> subscribed = friendsService.getSubscribed(principal.getName());
        model.addAttribute("subscribed", subscribed);
        return "friends/subscribed";
    }
}
