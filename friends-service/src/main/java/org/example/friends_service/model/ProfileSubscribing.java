package org.example.friends_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profile_subscribing")
public class ProfileSubscribing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Profile> subscribing = new ArrayList<>();

    public void addFriend(Profile friend) {
        if(!subscribing .contains(friend)) {
            subscribing .add(friend);
        }
    }

    public void deleteFriend(Profile friend) {
        subscribing .remove(friend);
    }
}
