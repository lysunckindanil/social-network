package org.example.subscriberservice.repo;

import org.example.subscriberservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("select p from Profile p where p.username=?1")
    Optional<Profile> findByUsername(String username);
}