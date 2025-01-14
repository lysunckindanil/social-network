package org.example.friends_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profile_friend")
public class ProfileFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    private Profile profile;

    @ManyToMany
    private List<Profile> friends = new ArrayList<>();

    public void addFriend(Profile friend) {
        friends.add(friend);
    }

    public void deleteFriend(Profile friend) {
        friends.remove(friend);
    }
}
