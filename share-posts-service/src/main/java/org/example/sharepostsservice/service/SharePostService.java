package org.example.sharepostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.sharepostsservice.dto.ShareFriendsDto;
import org.example.sharepostsservice.model.FriendPost;
import org.example.sharepostsservice.model.Post;
import org.example.sharepostsservice.model.Profile;
import org.example.sharepostsservice.model.ProfileFriend;
import org.example.sharepostsservice.repo.FriendPostRepository;
import org.example.sharepostsservice.repo.PostRepository;
import org.example.sharepostsservice.repo.ProfileFriendRepository;
import org.example.sharepostsservice.repo.ProfileRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SharePostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final ProfileFriendRepository profileFriendRepository;
    private final FriendPostRepository friendPostRepository;

    @KafkaListener(topics = "share-friends", groupId = "share-posts-service")
    public void sharePostsToFriends(ShareFriendsDto dto) {
        // find profile whose friends to be shared
        Profile profile = profileRepository.findById(dto.getProfile_id()).orElseThrow(() -> new RuntimeException("User not found"));

        // find friends of the profile
        Optional<ProfileFriend> friends_optional = profileFriendRepository.findProfileFriendByProfile(profile);
        if (friends_optional.isEmpty()) return;
        List<Profile> friends = friends_optional.get().getFriends();
        if (friends.isEmpty()) return;

        // find post
        Optional<Post> post_optional = postRepository.findById(dto.getPost_id());
        if (post_optional.isEmpty()) return;
        Post post = post_optional.get();

        // add post to every friend
        friends.forEach((friend) -> {
            Optional<FriendPost> friendPost_optional = friendPostRepository.findFriendPostByProfile(friend);
            FriendPost friendPost;
            if (friendPost_optional.isPresent()) {
                friendPost = friendPost_optional.get();
                friendPost.addPost(post);
            } else {
                friendPost = new FriendPost();
                friendPost.setProfile(friend);
                friendPost.addPost(post);
            }
            friendPostRepository.save(friendPost);
        });
    }

    @KafkaListener(topics = "delete-from-friends", groupId = "share-posts-service")
    public void deletePostsFromFriends(ShareFriendsDto dto) {
        // find profile whose friends to be shared
        Optional<Profile> profile_optional = profileRepository.findById(dto.getProfile_id());
        if (profile_optional.isEmpty()) return;
        Profile profile = profile_optional.get();

        // find friends of the profile
        Optional<ProfileFriend> friends_optional = profileFriendRepository.findProfileFriendByProfile(profile);
        if (friends_optional.isEmpty()) return;
        List<Profile> friends = friends_optional.get().getFriends();
        if (friends.isEmpty()) return;

        // find post
        Optional<Post> post_optional = postRepository.findById(dto.getPost_id());
        if (post_optional.isEmpty()) return;
        Post post = post_optional.get();

        // delete post from every friend
        friends.forEach((friend) -> {
            Optional<FriendPost> friendPost_optional = friendPostRepository.findFriendPostByProfile(friend);
            FriendPost friendPost;
            if (friendPost_optional.isPresent()) {
                friendPost = friendPost_optional.get();
                friendPost.deletePost(post);
                friendPostRepository.save(friendPost);
            }
        });

        // delete post itself
        postRepository.deleteById(dto.getPost_id());
    }
}
