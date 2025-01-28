package org.example.subscriberpostsservice.repo;

import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.PostSubscriber;
import org.example.subscriberpostsservice.model.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.List;

public interface PostSubscriberRepository extends JpaRepository<PostSubscriber, PostSubscriber.Id> {
    @Query("select p.post from PostSubscriber p join fetch p.post.author where p.subscriber=:subscriber")
    Streamable<Post> findPostsBySubscriber(Profile subscriber);

    @Query("select p.post from PostSubscriber p join fetch p.post.author where p.subscriber=:subscriber order by p.post.createdAt desc")
    List<Post> findPostsBySubscriberPageableOrderByCreatedAtDesc(Profile subscriber, Pageable pageable);

}