package org.example.postsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.AddPostDto;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.GetPostsPageableDto;
import org.example.postsservice.dto.PostDto;
import org.example.postsservice.model.Post;
import org.example.postsservice.model.Profile;
import org.example.postsservice.repo.PostRepository;
import org.example.postsservice.repo.ProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<PostDto> getPostsByProfileUsernamePageable(GetPostsPageableDto dto) {
        String username = dto.getProfileUsername();
        int page = dto.getPage();
        int size = dto.getSize();
        Optional<Profile> author = profileRepository.findByUsername(username);
        if (author.isEmpty())
            return new ArrayList<>();

        Sort.TypedSort<Post> post = Sort.sort(Post.class);
        Sort sort = post.by(Post::getCreatedAt).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return postRepository.findAllByAuthor(author.get(), pageRequest)
                .stream()
                .map(PostsService::wrapPost)
                .toList();
    }

    public void addPostByUsername(AddPostDto postDto) {
        Post post = unwrapPost(postDto.getPost());
        Optional<Profile> authorOptional = profileRepository.findByUsername(postDto.getProfileUsername());
        if (authorOptional.isEmpty()) {
            return;
        }
        Profile author = authorOptional.get();
        post.setAuthor(author);
        postRepository.save(post);
        shareSubscribersClient.shareSubscribers(author.getId(), post.getId());
    }

    public void deletePost(DeletePostDto postDto) {
        Optional<Post> postToDeleteOptional = postRepository.findById(postDto.getPostId());
        if (postToDeleteOptional.isPresent()) {
            Post postToDelete = postToDeleteOptional.get();
            postToDelete.setAuthor(null);
            postRepository.save(postToDelete);
            shareSubscribersClient.deleteFromSubscribers(postToDelete.getId());
        }
    }


    private static Post unwrapPost(PostDto post) {
        Post postToUnwrap = new Post();
        postToUnwrap.setId(post.getId());
        postToUnwrap.setLabel(post.getLabel());
        postToUnwrap.setContent(post.getContent());
        postToUnwrap.setCreatedAt(post.getCreatedAt());
        return postToUnwrap;
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
