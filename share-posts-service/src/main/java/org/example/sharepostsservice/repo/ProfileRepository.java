package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}