package org.example.friendpostsservice.repo;

import org.example.friendpostsservice.model.ProfileSubscribedByPost;
import org.example.friendpostsservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribedByPostRepository extends JpaRepository<ProfileSubscribedByPost, Integer> {
    @Query("select p from ProfileSubscribedByPost p where p.profile=:profile")
    Optional<ProfileSubscribedByPost> findFriendPostByProfile(@Param("profile") Profile profile);
}
