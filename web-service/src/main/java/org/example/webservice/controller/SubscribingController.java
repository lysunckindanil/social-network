package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.ProfileDto;
import org.example.webservice.service.SubscriberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("subscribing")
@RequiredArgsConstructor
@Controller
public class SubscribingController {
    private final SubscriberService subscriberService;

    @GetMapping()
    public String subscribing(Principal principal, Model model) {
        List<String> subscribing = subscriberService.findProfileSubscribedOn(principal.getName()).stream().map(ProfileDto::getUsername).toList();
        model.addAttribute("subscribing", subscribing);
        model.addAttribute("username", principal.getName());
        return "friends/subscribing";
    }

    @PostMapping("/subscribe")
    public String subscribe(Principal principal, @RequestParam("friend_username") String friend_username) {
        subscriberService.subscribe(principal.getName(), friend_username);
        return "redirect:/profile/" + friend_username;
    }


    @PostMapping("/unsubscribe")
    public String unsubscribe(Principal principal, @RequestParam("friend_username") String friend_username) {
        subscriberService.unsubscribe(principal.getName(), friend_username);
        return "redirect:/profile/" + friend_username;
    }
}
