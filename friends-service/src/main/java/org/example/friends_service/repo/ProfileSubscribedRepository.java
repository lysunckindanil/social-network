package org.example.friends_service.repo;

import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribedRepository extends JpaRepository<ProfileSubscribed, Long> {

    Optional<ProfileSubscribed> findProfileSubscribedByProfile(Profile profile);
}