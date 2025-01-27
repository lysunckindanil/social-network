package org.example.postsservice.repo;

import org.example.postsservice.model.Post;
import org.example.postsservice.model.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(Profile author);

    List<Post> findByAuthor(Profile author, Pageable pageable);
}