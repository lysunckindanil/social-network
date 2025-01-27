package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}