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

import java.util.ArrayList;
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
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        Optional<ProfilePost> optionalProfilePosts = profilePostRepository.findProfilePostByProfile(profile);
        if (optionalProfilePosts.isPresent()) {
            return optionalProfilePosts.get().getPosts().stream().map(PostsService::wrapPost).toList();
        }
        return new ArrayList<>();
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

        shareFriendsClient.shareFriends(profile.getId(), post.getId());
        profilePost.addPost(post);
        profilePostRepository.save(profilePost);
    }

    public void deletePostByUsername(AddAndDeletePostDto postDto) {
        String username = postDto.getProfile_username();
        Post post = unwrapPost(postDto.getPost());
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
        ProfilePost profilePost = profilePostRepository.findProfilePostByProfile(profile).orElseThrow(() -> new RuntimeException("User doesnt have posts"));
        Post postToDelete = profilePost.getPosts().stream().filter((p) -> p.equals(post)).findFirst().orElseThrow(() -> new RuntimeException("Post doesnt exist"));
        profilePost.deletePost(post);
        profilePostRepository.save(profilePost);
        postRepository.delete(postToDelete);
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
