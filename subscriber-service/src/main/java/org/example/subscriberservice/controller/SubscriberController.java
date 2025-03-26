package org.example.subscriberservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.dto.GetSubscribersPageableDto;
import org.example.subscriberservice.dto.IsSubscriberDto;
import org.example.subscriberservice.dto.ProfileDto;
import org.example.subscriberservice.service.SubscriberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/isSubscribedOn")
    public Boolean isSubscribedOn(@Valid @RequestBody IsSubscriberDto dto) {
        return subscriberService.isProfileSubscribedOn(dto);
    }

    @PostMapping("/findProfileSubscribedByPageable")
    public List<ProfileDto> findProfileSubscribedByPageable(@Valid @RequestBody GetSubscribersPageableDto dto) {
        return subscriberService.findProfileSubscribedByPageable(dto);
    }

    @PostMapping("/findProfileSubscribedOnPageable")
    public List<ProfileDto> findProfileSubscribedOnPageable(@Valid @RequestBody GetSubscribersPageableDto dto) {
        return subscriberService.findProfileSubscribedOnPageable(dto);
    }

    @PostMapping("/addSubscriber")
    public ResponseEntity<Void> addSubscriber(@Valid @RequestBody AddAndDeleteSubscriberDto dto) {
        subscriberService.addSubscriber(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deleteSubscriber")
    public ResponseEntity<Void> deleteSubscriber(@Valid @RequestBody AddAndDeleteSubscriberDto dto) {
        subscriberService.deleteSubscriber(dto);
        return ResponseEntity.noContent().build();
    }
}
