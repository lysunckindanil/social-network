package org.example.subscriberservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

@Immutable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NotNull
@Getter
@Entity
@Table(name = "profile_subscriber")
public class ProfileSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    private Profile profile;

    @NotNull
    @ManyToOne
    private Profile subscriber;
}
