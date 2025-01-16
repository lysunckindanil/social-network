package org.example.sharepostsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sharepostsservice.dto.ShareFriendsDto;
import org.example.sharepostsservice.model.ProfileSubscribedByPost;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileSubscribedBy;
import org.example.sharepostsservice.repo.ProfileSubscribedByPostRepository;
import org.example.sharepostsservice.repo.PostRepository;
import org.example.sharepostsservice.repo.ProfileSubscribedByRepository;
import org.example.sharepostsservice.repo.ProfileRepository;
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
    private final ProfileSubscribedByRepository profileSubscribedByRepository;
    private final ProfileSubscribedByPostRepository profileSubscribedByPostRepository;

    @KafkaListener(topics = "share-friends", groupId = "share-posts-service")
    public void sharePostsToFriends(ShareFriendsDto dto) {
        log.info("sharePostsToFriends: {}", dto);
        log.info("sharePostsToFriends: {}", dto.getProfile_id());
        log.info("sharePostsToFriends: {}", dto.getPost_id());
        // find profile whose friends to be shared
        Profile profile = profileRepository.findById(dto.getProfile_id()).orElseThrow(() -> new RuntimeException("User not found"));

        // find friends of the profile
        Optional<ProfileSubscribedBy> friends_optional = profileSubscribedByRepository.findSubscribedProfilesByProfile(profile);
        if (friends_optional.isEmpty()) return;
        List<Profile> friends = friends_optional.get().getProfiles();
        if (friends.isEmpty()) return;

        // find post
        Optional<Post> post_optional = postRepository.findById(dto.getPost_id());
        if (post_optional.isEmpty()) return;
        Post post = post_optional.get();

        // add post to every friend
        friends.forEach((friend) -> {
            Optional<ProfileSubscribedByPost> friendPost_optional = profileSubscribedByPostRepository.findProfileSubscribedByPosts(friend);
            ProfileSubscribedByPost profileSubscribedByPost;
            if (friendPost_optional.isPresent()) {
                profileSubscribedByPost = friendPost_optional.get();
            } else {
                profileSubscribedByPost = new ProfileSubscribedByPost();
                profileSubscribedByPost.setProfile(friend);
            }
            profileSubscribedByPost.addPost(post);
            profileSubscribedByPostRepository.save(profileSubscribedByPost);
        });
    }

    @KafkaListener(topics = "delete-from-friends", groupId = "share-posts-service")
    public void deletePostsFromFriends(ShareFriendsDto dto) {
        // find profile whose friends to be shared
        Optional<Profile> profile_optional = profileRepository.findById(dto.getProfile_id());
        if (profile_optional.isEmpty()) return;
        Profile profile = profile_optional.get();

        // find friends of the profile
        Optional<ProfileSubscribedBy> friends_optional = profileSubscribedByRepository.findSubscribedProfilesByProfile(profile);
        if (friends_optional.isEmpty()) return;
        List<Profile> friends = friends_optional.get().getProfiles();
        if (friends.isEmpty()) return;

        // find post
        Optional<Post> post_optional = postRepository.findById(dto.getPost_id());
        if (post_optional.isEmpty()) return;
        Post post = post_optional.get();

        // delete post from every friend
        friends.forEach((friend) -> {
            Optional<ProfileSubscribedByPost> friendPost_optional = profileSubscribedByPostRepository.findProfileSubscribedByPosts(friend);
            ProfileSubscribedByPost profileSubscribedByPost;
            if (friendPost_optional.isPresent()) {
                profileSubscribedByPost = friendPost_optional.get();
                profileSubscribedByPost.deletePost(post);
                profileSubscribedByPostRepository.save(profileSubscribedByPost);
            }
        });

        // delete post itself
        postRepository.deleteById(dto.getPost_id());
    }
}
