package org.example.friendspostsservice.repo;

import org.example.friendspostsservice.model.Profile;
import org.example.friendspostsservice.model.ProfileFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileFriendRepository extends JpaRepository<ProfileFriend, Long> {
    Optional<ProfileFriend> findProfileFriendByProfile(Profile profile);
}