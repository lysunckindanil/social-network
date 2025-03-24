package org.example.subscriberservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.dto.GetSubscribersPageableDto;
import org.example.subscriberservice.dto.IsSubscriberDto;
import org.example.subscriberservice.dto.ProfileDto;
import org.example.subscriberservice.service.SubscriberService;
import org.example.subscriberservice.util.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/isSubscribedOn")
    public Boolean isSubscribedOn(@Valid @RequestBody IsSubscriberDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return subscriberService.isProfileSubscribedOn(dto);
    }

    @PostMapping("/findProfileSubscribedByPageable")
    public List<ProfileDto> findProfileSubscribedByPageable(@Valid @RequestBody GetSubscribersPageableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return subscriberService.findProfileSubscribedByPageable(dto);
    }

    @PostMapping("/findProfileSubscribedOnPageable")
    public List<ProfileDto> findProfileSubscribedOnPageable(@Valid @RequestBody GetSubscribersPageableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return subscriberService.findProfileSubscribedOnPageable(dto);
    }

    @PostMapping("/addSubscriber")
    public ResponseEntity<Void> addSubscriber(@Valid @RequestBody AddAndDeleteSubscriberDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        subscriberService.addSubscriber(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deleteSubscriber")
    public ResponseEntity<Void> deleteSubscriber(@Valid @RequestBody AddAndDeleteSubscriberDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        subscriberService.deleteSubscriber(dto);
        return ResponseEntity.noContent().build();
    }
}
