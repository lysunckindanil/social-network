package org.example.subscriberpostsservice.model;

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
@Table(name = "profile_subscriber")
public class ProfileSubscriber {
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "profile_id")
        private Long profileId;
        @Column(name = "subscriber_id")
        private Long subscriberId;

        public Id() {
        }

        public Id(Long profileId, Long subscriberId) {
            this.profileId = profileId;
            this.subscriberId = subscriberId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(profileId, id.profileId) && Objects.equals(subscriberId, id.subscriberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(profileId, subscriberId);
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @MapsId("profileId")
    @ManyToOne
    @JoinColumn(
            name = "profile_id", insertable = false, updatable = false)
    private Profile profile;


    @MapsId("subscriberId")
    @ManyToOne
    @JoinColumn(
            name = "subscriber_id", insertable = false, updatable = false)
    private Profile subscriber;

    public ProfileSubscriber(Profile profile, Profile subscriber) {
        this.id = new Id(profile.getId(), subscriber.getId());
        this.profile = profile;
        this.subscriber = subscriber;
    }
}
