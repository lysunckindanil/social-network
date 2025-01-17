package org.example.friendpostsservice.repo;

import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.PostSubscriber;
import org.example.friendpostsservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostSubscriberRepository extends JpaRepository<PostSubscriber, Long> {
    @Query("select p.post from PostSubscriber p where p.subscriber=:subscriber")
    List<Post> findPostsBySubscriber(Profile subscriber);
}