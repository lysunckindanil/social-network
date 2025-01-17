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
    public String subscribe(Principal principal, @RequestParam("to_be_subscribed") String to_be_subscribed) {
        subscriberService.subscribe(to_be_subscribed, principal.getName());
        return "redirect:/profile/" + to_be_subscribed;
    }


    @PostMapping("/unsubscribe")
    public String unsubscribe(Principal principal, @RequestParam("to_be_unsubscribed") String to_be_unsubscribed) {
        subscriberService.unsubscribe(to_be_unsubscribed, principal.getName());
        return "redirect:/profile/" + to_be_unsubscribed;
    }
}
