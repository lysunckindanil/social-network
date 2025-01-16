package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileSubscriberRepository extends JpaRepository<ProfileSubscriber, Long> {

    List<ProfileSubscriber> findAllByProfile(Profile profile);

}