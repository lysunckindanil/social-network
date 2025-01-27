package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileSubscriberRepository extends JpaRepository<ProfileSubscriber, ProfileSubscriber.Id> {

    List<ProfileSubscriber> findByProfile(Profile profile);

}