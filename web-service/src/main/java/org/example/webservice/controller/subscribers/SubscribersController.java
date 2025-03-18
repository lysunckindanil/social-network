package org.example.webservice.controller.subscribers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("subscribing")
@RequiredArgsConstructor
@Controller
public class SubscribersController {

    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/on")
    public String subscribing() {
        return "friends/subscribing";
    }

    @GetMapping("/by")
    public String subscribed() {
        return "friends/subscribed";
    }
}
