package org.example.friends_service.repo;

import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileSubscribedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribedByRepository extends JpaRepository<ProfileSubscribedBy, Long> {

    @Query("select p from ProfileSubscribedBy p where p.profile=:profile")
    Optional<ProfileSubscribedBy> findProfileSubscribersByProfile(@Param("profile") Profile profile);
}