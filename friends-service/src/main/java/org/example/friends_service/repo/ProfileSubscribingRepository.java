package org.example.friends_service.repo;

import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribingRepository extends JpaRepository<ProfileSubscribing, Long> {
    Optional<ProfileSubscribing> findProfileSubscribingByProfile(Profile profile);
}