package org.example.friendpostsservice.repo;

import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.model.ProfilePostSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePostSubscribedRepository extends JpaRepository<ProfilePostSubscriber, Long> {
    Optional<ProfilePostSubscriber> findProfilePostSubscribedByProfile(Profile profile);
}