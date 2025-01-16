package org.example.postsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.AddAndDeletePostDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Post;
import org.example.postsservice.model.Profile;
import org.example.postsservice.model.ProfilePost;
import org.example.postsservice.repo.PostRepository;
import org.example.postsservice.repo.ProfilePostRepository;
import org.example.postsservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;
    private final ProfilePostRepository profilePostRepository;
    private final ShareFriendsClient shareFriendsClient;

    public List<PostDto> getPostsByProfileUsername(String username) {
        return profilePostRepository.findPostsByProfile(username)
                .stream()
                .map(PostsService::wrapPost)
                .toList();
    }

    public void addPostByUsername(AddAndDeletePostDto postDto) {
        String username = postDto.getProfile_username();
        Post post = unwrapPost(postDto.getPost());
        post.setCreatedAt(new Date());
        postRepository.save(post);

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfilePost> optionalProfilePosts = profilePostRepository.findProfilePostByProfile(profile);
        ProfilePost profilePost;
        if (optionalProfilePosts.isPresent()) {
            profilePost = optionalProfilePosts.get();
        } else {
            profilePost = new ProfilePost();
            profilePost.setProfile(profile);
        }
        profilePost.addPost(post);
        profilePostRepository.save(profilePost);
        shareFriendsClient.shareFriends(profile.getId(), post.getId());
    }

    public void deletePostByUsername(AddAndDeletePostDto postDto) {
        String username = postDto.getProfile_username();
        Post post = unwrapPost(postDto.getPost());
        post.setCreatedAt(new Date());
        postRepository.save(post);

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfilePost> optionalProfilePosts = profilePostRepository.findProfilePostByProfile(profile);
        ProfilePost profilePost;
        if (optionalProfilePosts.isPresent()) {
            profilePost = optionalProfilePosts.get();
            profilePost.deletePost(post);
            profilePostRepository.save(profilePost);
            shareFriendsClient.deleteFromFriends(profile.getId(), post.getId());
        }

    }


    private static Post unwrapPost(PostDto post) {
        return Post.builder()
                .id(post.getId())
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private static PostDto wrapPost(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
