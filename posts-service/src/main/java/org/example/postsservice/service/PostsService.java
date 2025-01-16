package org.example.postsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.AddAndDeletePostDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Post;
import org.example.postsservice.model.Profile;
import org.example.postsservice.repo.PostRepository;
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
    private final ShareSubscribersClient shareSubscribersClient;

    public List<PostDto> getPostsByProfileUsername(String username) {
        Optional<Profile> author = profileRepository.findByUsername(username);
        if (author.isEmpty())
            return new ArrayList<>();

        return postRepository.findAllByAuthor(author.get())
                .stream()
                .map(PostsService::wrapPost)
                .toList();
    }

    public void addPostByUsername(AddAndDeletePostDto postDto) {
        Post post = unwrapPost(postDto.getPost());
        post.setCreatedAt(new Date());
        Optional<Profile> authorOptional = profileRepository.findByUsername(postDto.getProfile_username());
        if (authorOptional.isEmpty()) {
            return;
        }
        Profile author = authorOptional.get();
        post.setAuthor(author);
        postRepository.save(post);
        shareSubscribersClient.shareSubscribers(post.getId(), author.getId());
    }

    public void deletePostByUsername(AddAndDeletePostDto postDto) {
        Post post = unwrapPost(postDto.getPost());
        Optional<Profile> authorOptional = profileRepository.findByUsername(postDto.getProfile_username());
        if (authorOptional.isEmpty()) {
            return;
        }
        Profile author = authorOptional.get();
        List<Post> posts = postRepository.findAllByAuthor(author);
        Optional<Post> postToDeleteOptional = posts.stream().filter(p -> p.getCreatedAt().equals(post.getCreatedAt())).findFirst();
        if (postToDeleteOptional.isPresent()) {
            Post postToDelete = postToDeleteOptional.get();
            postToDelete.setAuthor(null);
            postRepository.save(postToDelete);
            shareSubscribersClient.deleteFromSubscribers(postToDelete.getId(), author.getId());
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
                .author(post.getAuthor().getUsername())
                .build();
    }
}
