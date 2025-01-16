package org.example.sharepostsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sharepostsservice.dto.ShareFriendsDto;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfilePostSubscriber;
import org.example.sharepostsservice.model.ProfileSubscriber;
import org.example.sharepostsservice.repo.PostRepository;
import org.example.sharepostsservice.repo.ProfilePostSubscribedRepository;
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
    private final ProfilePostSubscribedRepository profilePostSubscribedRepository;

    @KafkaListener(topics = "share-subscribers", groupId = "share-posts-service")
    public void sharePostsToSubscribers(ShareFriendsDto dto) {
        // find profile whose friends to be shared
        Optional<Profile> profileOptional = profileRepository.findById(dto.getProfile_id());
        if (profileOptional.isEmpty()) return;
        Profile profile = profileOptional.get();

        // find subscribers of the profile
        List<Profile> subscribers = profileSubscriberRepository.findAllByProfile(profile).stream()
                .map(ProfileSubscriber::getSubscriber).toList();

        // find post
        Optional<Post> post_optional = postRepository.findById(dto.getPost_id());
        if (post_optional.isEmpty()) return;
        Post post = post_optional.get();

        // add post to every friend
        subscribers.forEach((sub) -> {
            Optional<ProfilePostSubscriber> friendPost_optional = profilePostSubscribedRepository.findProfilePostSubscribedByProfile(sub);
            ProfilePostSubscriber profilePostSubscriber;
            if (friendPost_optional.isPresent()) {
                profilePostSubscriber = friendPost_optional.get();
            } else {
                profilePostSubscriber = new ProfilePostSubscriber();
                profilePostSubscriber.setProfile(sub);
            }
            profilePostSubscriber.addPost(post);
            profilePostSubscribedRepository.save(profilePostSubscriber);
        });
    }

    @KafkaListener(topics = "delete-from-subscribers", groupId = "share-posts-service")
    public void deletePostsFromSubscribers(ShareFriendsDto dto) {
        // find profile whose friends to be shared
        Optional<Profile> profile_optional = profileRepository.findById(dto.getProfile_id());
        if (profile_optional.isEmpty()) return;
        Profile profile = profile_optional.get();

        // find subscribers of the profile
        List<Profile> subscribers = profileSubscriberRepository.findAllByProfile(profile).stream()
                .map(ProfileSubscriber::getSubscriber).toList();

        // find post
        Optional<Post> post_optional = postRepository.findById(dto.getPost_id());
        if (post_optional.isEmpty()) return;
        Post post = post_optional.get();

        // delete post from every friend
        subscribers.forEach((sub) -> {
            Optional<ProfilePostSubscriber> friendPost_optional = profilePostSubscribedRepository.findProfilePostSubscribedByProfile(sub);
            ProfilePostSubscriber profilePostSubscriber;
            if (friendPost_optional.isPresent()) {
                profilePostSubscriber = friendPost_optional.get();
                profilePostSubscriber.deletePost(post);
                profilePostSubscribedRepository.save(profilePostSubscriber);
            }
        });

        // delete post itself
        postRepository.delete(post);
    }
}
