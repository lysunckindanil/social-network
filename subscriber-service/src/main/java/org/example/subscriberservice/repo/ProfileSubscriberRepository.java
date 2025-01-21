package org.example.subscriberservice.repo;

import org.example.subscriberservice.model.Profile;
import org.example.subscriberservice.model.ProfileSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileSubscriberRepository extends JpaRepository<ProfileSubscriber, ProfileSubscriber.Id> {

    List<ProfileSubscriber> findByProfile(Profile profile);

    List<ProfileSubscriber> findBySubscriber(Profile profile);

    Optional<ProfileSubscriber> findByProfileAndSubscriber(Profile profile, Profile subscriber);

}