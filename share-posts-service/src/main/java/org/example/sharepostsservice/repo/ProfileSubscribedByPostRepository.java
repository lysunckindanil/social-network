package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.ProfileSubscribedByPost;
import org.example.sharepostsservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSubscribedByPostRepository extends JpaRepository<ProfileSubscribedByPost, Integer> {
    @Query("select p from ProfileSubscribedByPost p where p.profile=:profile")
    Optional<ProfileSubscribedByPost> findProfileSubscribedByPosts(@Param("profile") Profile profile);
}
