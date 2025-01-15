package org.example.webservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.webservice.service.FriendsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("friends")
@RequiredArgsConstructor
@Controller
public class SubscribingController {
    private final FriendsService friendsService;

    @GetMapping
    public String subscribing(Principal principal, Model model) {
        List<String> subscribing = friendsService.getSubscribing(principal.getName());
        model.addAttribute("subscribing", subscribing);
        return "friends/subscribing";
    }

    @PostMapping("/addFriend")
    public String addFriend(Principal principal, @RequestParam("friend_username") String friend_username) {
        friendsService.addFriend(principal.getName(), friend_username);
        return "redirect:/profile/" + friend_username;
    }


    @PostMapping("/deleteFriend")
    public String deleteFriend(Principal principal, @RequestParam("friend_username") String friend_username) {
        friendsService.deleteFriend(principal.getName(), friend_username);
        return "redirect:/profile/" + friend_username;
    }
}
