package org.example.webservice.service;

import org.example.webservice.dto.profiles.ProfileDto;

import java.util.List;

public interface SubscriberService {
    List<ProfileDto> findProfileSubscribedBy(String username, int page);

    List<ProfileDto> findProfileSubscribedOn(String username, int page);

    Boolean isISubscribedOn(String subscriberUsername, String profileUsername);

    void subscribe(String profileUsername, String subscriberUsername);

    void unsubscribe(String profileUsername, String subscriberUsername);
}
