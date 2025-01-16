package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfilePostSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePostSubscribedRepository extends JpaRepository<ProfilePostSubscriber, Long> {

    Optional<ProfilePostSubscriber> findProfilePostSubscribedByProfile(Profile profile);
}