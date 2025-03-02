package org.example.subscriberservice.repo;

import org.example.subscriberservice.model.Profile;
import org.example.subscriberservice.model.ProfileSubscriber;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileSubscriberRepository extends JpaRepository<ProfileSubscriber, ProfileSubscriber.Id> {

    List<ProfileSubscriber> findByProfile(Profile profile, Pageable pageable);

    List<ProfileSubscriber> findBySubscriber(Profile profile, Pageable pageable);

    Optional<ProfileSubscriber> findByProfileAndSubscriber(Profile profile, Profile subscriber);
}