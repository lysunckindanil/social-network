package org.example.subscriberpostsservice.repo;

import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.PostSubscriber;
import org.example.subscriberpostsservice.model.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostSubscriberRepository extends JpaRepository<PostSubscriber, PostSubscriber.Id> {
    @Query("select p.post from PostSubscriber p where p.subscriber=:subscriber and p.post.author!=null")
    List<Post> findPostsBySubscriber(Profile subscriber);

    @Query("select p.post from PostSubscriber p where p.subscriber=:subscriber and p.post.author!=null order by p.post.createdAt desc")
    List<Post> findPostsBySubscriberPageableOrderByCreatedAtDesc(Profile subscriber, Pageable pageable);

}