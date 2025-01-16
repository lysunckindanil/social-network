package org.example.friends_service.model;

import jakarta.persistence.*;
import lombok.*;

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

    public void addProfile(Profile friend) {
        if (!profiles.contains(friend)) {
            profiles.add(friend);
        }
    }

    public void deleteProfile(Profile friend) {
        profiles.remove(friend);
    }
}
