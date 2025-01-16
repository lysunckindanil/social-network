package org.example.friendpostsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profile_subscribed_by_post")
public class ProfileSubscribedByPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();
}