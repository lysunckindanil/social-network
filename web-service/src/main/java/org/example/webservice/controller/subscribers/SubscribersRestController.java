package org.example.webservice.controller.subscribers;

import lombok.RequiredArgsConstructor;
import org.example.webservice.dto.profiles.ProfileDto;
import org.example.webservice.service.subscribers.SubscriberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping("subscribing")
@RequiredArgsConstructor
@RestController
public class SubscribersRestController {
    private final SubscriberService subscriberService;

    @PostMapping("/onPageable")
    public List<ProfileDto> subscribing(Principal principal, @RequestParam int page) {
        return subscriberService.findProfileSubscribedOn(principal.getName(), page);
    }

    @PostMapping("/byPageable")
    public List<ProfileDto> subscribed(Principal principal, @RequestParam int page) {
        return subscriberService.findProfileSubscribedBy(principal.getName(), page);
    }

    @PostMapping("/isSubscribedOn")
    public Boolean isSubscribedOn(@RequestParam("profileUsername") String profileUsername, Principal principal) {
        return subscriberService.isISubscribedOn(principal.getName(), profileUsername);
    }

    @PostMapping("/subscribe")
    public String subscribe(Principal principal, @RequestParam("profileUsername") String to_be_subscribed) {
        subscriberService.subscribe(to_be_subscribed, principal.getName());
        return "subscribed";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(Principal principal, @RequestParam("profileUsername") String to_be_unsubscribed) {
        subscriberService.unsubscribe(to_be_unsubscribed, principal.getName());
        return "unsubscribed";
    }
}
