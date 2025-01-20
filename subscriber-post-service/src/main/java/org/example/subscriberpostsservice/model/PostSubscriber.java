package org.example.subscriberpostsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

@Immutable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_subscriber")
public class PostSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    private Post post;

    @NotNull
    @ManyToOne
    private Profile subscriber;
}