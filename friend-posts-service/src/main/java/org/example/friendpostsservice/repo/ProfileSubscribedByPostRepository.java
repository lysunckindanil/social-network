package org.example.friendpostsservice.repo;

import org.example.friendpostsservice.model.ProfileSubscriberByPost;
import org.example.friendpostsservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribedByPostRepository extends JpaRepository<ProfileSubscriberByPost, Integer> {
    @Query("select p from ProfileSubscriberByPost p where p.profile=:profile")
    Optional<ProfileSubscriberByPost> findFriendPostByProfile(@Param("profile") Profile profile);
}
