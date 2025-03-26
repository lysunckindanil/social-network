package org.example.subscriberpostsservice.repo;

import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select post from Post post join ProfileSubscriber pf on post.author.id = pf.profile.id where pf.subscriber = :profile")
    List<Post> findSubscribedPostsByProfile(@Param("profile") Profile profile, PageRequest pageRequest);
}