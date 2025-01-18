package org.example.sharepostsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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