package org.example.subscriberpostsservice.repo;

import org.example.subscriberpostsservice.model.ProfileSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileSubscriberRepository extends JpaRepository<ProfileSubscriber, ProfileSubscriber.Id> {
}