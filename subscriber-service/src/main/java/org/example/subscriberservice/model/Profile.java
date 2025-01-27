package org.example.subscriberservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    @Column(unique = true)
    private String username;
    @NotEmpty
    private String password;
    private String email;
    private String photoUrl;

    // just for test, generally retrieved from join table
    @OneToMany(mappedBy = "profile")
    private List<ProfileSubscriber> subscribers = new ArrayList<>();

    @OneToMany(mappedBy = "subscriber")
    private List<ProfileSubscriber> subscribing = new ArrayList<>();
}
