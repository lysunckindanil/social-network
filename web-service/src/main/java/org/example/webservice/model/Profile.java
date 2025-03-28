package org.example.webservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 6, max = 20, message = "Username should be between 6 and 20 letters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 6, message = "Password should be between 6 and 20 letters")
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = {@JoinColumn(
                    name = "profile_id", referencedColumnName = "id",
                    nullable = false
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "role_id", referencedColumnName = "id",
                    nullable = false
            )}
    )
    private List<Role> roles = new ArrayList<>();
}
