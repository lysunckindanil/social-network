package org.example.subscriberpostsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @NotEmpty
    private String label;
    @NotEmpty
    private String content;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "author_id")
    @ManyToOne()
    private Profile author;
}
