package org.example.sharepostsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profile_subscribed_by")
public class ProfileSubscribedBy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Profile> profiles = new ArrayList<>();

    public void addFriend(Profile friend) {
        if (!profiles.contains(friend)) {
            profiles.add(friend);
        }
    }

}
