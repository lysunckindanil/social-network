package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.profiles.ProfileDto;
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

    @GetMapping("/on")
    public String subscribing(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "friends/subscribing";
    }

    @GetMapping("/by")
    public String subscribed(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "friends/subscribed";
    }

    @ResponseBody
    @PostMapping("/onPageable")
    public List<ProfileDto> subscribing(Principal principal, @RequestParam int page) {
        return subscriberService.findProfileSubscribedOn(principal.getName(), page);
    }

    @ResponseBody
    @PostMapping("/byPageable")
    public List<ProfileDto> subscribed(Principal principal, @RequestParam int page) {
        return subscriberService.findProfileSubscribedBy(principal.getName(), page);
    }

    @ResponseBody
    @PostMapping("/isSubscribedOn")
    public Boolean isSubscribedOn(@RequestParam("profileUsername") String profileUsername, Principal principal) {
        return subscriberService.isISubscribedOn(principal.getName(), profileUsername);
    }

    //todo make pageable
    @ResponseBody
    @PostMapping("/subscribe")
    public String subscribe(Principal principal, @RequestParam("profileUsername") String to_be_subscribed) {
        subscriberService.subscribe(to_be_subscribed, principal.getName());
        return "subscribed";
    }

    //todo make pageable
    @ResponseBody
    @PostMapping("/unsubscribe")
    public String unsubscribe(Principal principal, @RequestParam("profileUsername") String to_be_unsubscribed) {
        subscriberService.unsubscribe(to_be_unsubscribed, principal.getName());
        return "unsubscribed";
    }
}
