package org.example.sharepostsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profile_post_subscriber")
public class ProfilePostSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.addFirst(post);
    }

    public void deletePost(Post post) {
        posts.remove(post);
    }
}