package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.FriendPost;
import org.example.sharepostsservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendPostRepository extends JpaRepository<FriendPost, Integer> {
    Optional<FriendPost> findFriendPostByProfile(Profile profile);
}
