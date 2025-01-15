package org.example.postsservice.repo;

import org.example.postsservice.model.Post;
import org.example.postsservice.model.Profile;
import org.example.postsservice.model.ProfilePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilePostRepository extends JpaRepository<ProfilePost, Long> {
    Optional<ProfilePost> findProfilePostByProfile(Profile profile);

    @Query("select p.posts from ProfilePost p where p.profile.username=?1")
    List<Post> findPostsByProfile(String username);
}