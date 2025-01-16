package org.example.friends_service.repo;

import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribeOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribedOnRepository extends JpaRepository<ProfileSubscribeOn, Long> {

    @Query("select p from ProfileSubscribeOn p where p.profile=:profile")
    Optional<ProfileSubscribeOn> findProfileSubscribingByProfile(@Param("profile") Profile profile);
}