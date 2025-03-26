package org.example.subscriberservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "photo_url")
    private String photoUrl;

    // just for test, generally retrieved from join table
    @OneToMany(mappedBy = "profile")
    private List<ProfileSubscriber> subscribers = new ArrayList<>();

    @OneToMany(mappedBy = "subscriber")
    private List<ProfileSubscriber> subscribing = new ArrayList<>();
}
