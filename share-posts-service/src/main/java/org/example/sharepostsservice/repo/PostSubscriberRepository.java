package org.example.sharepostsservice.repo;

import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.PostSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostSubscriberRepository extends JpaRepository<PostSubscriber, PostSubscriber.Id> {
    @Transactional
    void deleteByPost(Post post);
}