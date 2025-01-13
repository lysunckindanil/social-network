package org.example.friends_service.repo;

import org.example.friends_service.model.Profile;
import org.example.friends_service.model.ProfileFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileFriendRepository extends JpaRepository<ProfileFriend, Long> {

    List<ProfileFriend> findProfileFriendsByProfile(Profile profile);
}