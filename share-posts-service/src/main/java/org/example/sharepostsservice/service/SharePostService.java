package org.example.sharepostsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sharepostsservice.dto.DeletePostDto;
import org.example.sharepostsservice.dto.ShareSubscribersDto;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.PostSubscriber;
import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileSubscriber;
import org.example.sharepostsservice.repo.PostRepository;
import org.example.sharepostsservice.repo.PostSubscriberRepository;
import org.example.sharepostsservice.repo.ProfileRepository;
import org.example.sharepostsservice.repo.ProfileSubscriberRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharePostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final ProfileSubscriberRepository profileSubscriberRepository;
    private final PostSubscriberRepository postSubscriberRepository;

    @KafkaListener(topics = "share-subscribers", groupId = "share-posts-service")
    public void sharePostsToSubscribers(ShareSubscribersDto dto) {
        // find profile whose friends to be shared
        Optional<Profile> profileOptional = profileRepository.findById(dto.getProfileId());
        if (profileOptional.isEmpty()) return;
        Profile profile = profileOptional.get();

        // find subscribers of the profile
        List<Profile> subscribers = profileSubscriberRepository.findByProfile(profile).stream()
                .map(ProfileSubscriber::getSubscriber).toList();

        // find post
        Optional<Post> postOptional = postRepository.findById(dto.getPostId());
        if (postOptional.isEmpty()) return;
        Post post = postOptional.get();

        // add post to every friend
        postSubscriberRepository.saveAll(subscribers.stream().
                map(sub -> new PostSubscriber(post, sub)).toList());
    }

    @KafkaListener(topics = "delete-from-subscribers", groupId = "share-posts-service")
    public void deletePostsFromSubscribers(DeletePostDto dto) {
        // find post
        Optional<Post> postOptional = postRepository.findById(dto.getPostId());
        if (postOptional.isEmpty()) return;
        Post post = postOptional.get();

        // delete posts from users
        postSubscriberRepository.deleteByPost(post);
        // delete post itself
        postRepository.delete(post);
    }
}
