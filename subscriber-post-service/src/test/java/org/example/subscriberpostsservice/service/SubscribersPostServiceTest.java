package org.example.subscriberpostsservice.service;

import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.Profile;
import org.example.subscriberpostsservice.model.ProfileSubscriber;
import org.example.subscriberpostsservice.repo.PostRepository;
import org.example.subscriberpostsservice.repo.ProfileRepository;
import org.example.subscriberpostsservice.repo.ProfileSubscriberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubscribersPostServiceTest {
    @Autowired
    private ProfileSubscriberRepository profileSubscriberRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PostRepository postRepository;


    @Test
    void getSubscribersPosts_AddedPosts_ReturnsQuantityOfPostsPageable() {
        Profile profile = new Profile();
        profile.setUsername("user1");
        profile.setPassword("p");
        profileRepository.save(profile);

        Profile profile1 = new Profile();
        profile1.setUsername("user2");
        profile1.setPassword("p");
        profileRepository.save(profile1);

        Profile profile2 = new Profile();
        profile2.setUsername("user3");
        profile2.setPassword("p");
        profileRepository.save(profile2);

        // user1 subscribed by user2
        ProfileSubscriber profileSubscriber1 = new ProfileSubscriber(profile, profile1);
        profileSubscriberRepository.save(profileSubscriber1);

        // user1 has post
        Post post = new Post();
        post.setLabel("l");
        post.setContent("c");
        post.setAuthor(profile);
        postRepository.save(post);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Assertions.assertEquals(1, postRepository.findSubscribedPostsByProfile(profile1, pageRequest).size());
        Assertions.assertEquals(0, postRepository.findSubscribedPostsByProfile(profile, pageRequest).size());
        Assertions.assertEquals(0, postRepository.findSubscribedPostsByProfile(profile2, pageRequest).size());
    }
}