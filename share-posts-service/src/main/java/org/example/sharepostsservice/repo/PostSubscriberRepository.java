package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.PostSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostSubscriberRepository extends JpaRepository<PostSubscriber, Long> {
    void deleteAllByPost(Post post);
}