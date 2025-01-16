package org.example.friends_service.repo;

import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileSubscriberRepository extends JpaRepository<ProfileSubscriber, Long> {

    List<ProfileSubscriber> findAllByProfile(Profile profile);

    List<ProfileSubscriber> findAllBySubscriber(Profile profile);

    Optional<ProfileSubscriber> findByProfileAndSubscriber(Profile profile, Profile subscriber);

}