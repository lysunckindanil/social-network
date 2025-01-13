package org.example.friends_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id")
    private List<Profile> friends = new ArrayList<>();

    public void addFriend(Profile friend) {
        friends.add(friend);
    }
}
