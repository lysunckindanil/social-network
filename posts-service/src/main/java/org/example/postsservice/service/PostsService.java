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

    public List<PostDto> getPostsByProfileUsername(String username) {
        Optional<Profile> optionalProfile = profileRepository.findByUsername(username);
        if (optionalProfile.isPresent()) {
            Optional<ProfilePost> optionalProfilePosts = profilePostRepository.findProfilePostByProfile(optionalProfile.get());
            if (optionalProfilePosts.isPresent()) {
                return optionalProfilePosts.get().getPosts().stream().map(PostsService::wrapPost).toList();
            }
        }
        return new ArrayList<>();
    }

    public void addPostByUsername(AddAndDeletePostDto postDto) {
        String username = postDto.getProfile_username();
        Post post = unwrapPost(postDto.getPost());
        post.setCreatedAt(new Date());
        postRepository.save(post);
        Optional<Profile> optionalProfile = profileRepository.findByUsername(username);
        if (optionalProfile.isPresent()) {
            Optional<ProfilePost> optionalProfilePosts = profilePostRepository.findProfilePostByProfile(optionalProfile.get());
            ProfilePost profilePost;
            if (optionalProfilePosts.isPresent()) {
                profilePost = optionalProfilePosts.get();
            } else {
                profilePost = new ProfilePost();
                profilePost.setProfile(optionalProfile.get());
            }
            profilePost.addPost(post);
            profilePostRepository.save(profilePost);
        }
    }

    public void deletePostByUsername(AddAndDeletePostDto postDto) {
        String username = postDto.getProfile_username();
        Post post = unwrapPost(postDto.getPost());
        Optional<Profile> optionalProfile = profileRepository.findByUsername(username);
        if (optionalProfile.isPresent()) {
            Optional<ProfilePost> optionalProfilePosts = profilePostRepository.findProfilePostByProfile(optionalProfile.get());
            ProfilePost profilePost;
            if (optionalProfilePosts.isPresent()) {
                profilePost = optionalProfilePosts.get();
                Optional<Post> postToDelete = profilePost.getPosts().stream().filter((p) -> p.equals(post)).findFirst();
                if (postToDelete.isPresent()) {
                    profilePost.deletePost(post);
                    profilePostRepository.save(profilePost);
                    postRepository.delete(postToDelete.get());
                }
            }
        }
    }


    private static Post unwrapPost(PostDto post) {
        return Post.builder()
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
    private static PostDto wrapPost(Post post) {
        return PostDto.builder()
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
