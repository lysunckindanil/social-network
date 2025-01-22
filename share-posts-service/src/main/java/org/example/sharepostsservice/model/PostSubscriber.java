package org.example.sharepostsservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import java.io.Serializable;
import java.util.Objects;

@Immutable
@NoArgsConstructor
@Getter
@Entity
@Table(name = "post_subscriber")
public class PostSubscriber {
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "post_id")
        private Long postId;
        @Column(name = "subscriber_id")
        private Long subscriberId;

        public Id() {
        }

        public Id(Long postId, Long subscriberId) {
            this.postId = postId;
            this.subscriberId = subscriberId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(postId, id.postId) && Objects.equals(subscriberId, id.subscriberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(postId, subscriberId);
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(
            name = "post_id", insertable = false, updatable = false)
    private Post post;

    @MapsId("subscriberId")
    @ManyToOne
    @JoinColumn(
            name = "subscriber_id", insertable = false, updatable = false)
    private Profile subscriber;


    public PostSubscriber(Post post, Profile subscriber) {
        this.id = new Id(post.getId(), subscriber.getId());
        this.post = post;
        this.subscriber = subscriber;
    }
}