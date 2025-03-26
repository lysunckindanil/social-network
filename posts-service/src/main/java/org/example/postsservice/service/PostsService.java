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
import org.example.postsservice.util.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;
    private final ShareSubscribersClient shareSubscribersClient;

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByProfileUsernamePageable(GetPostsPageableDto dto) {
        String username = dto.getProfileUsername();
        Optional<Profile> author = profileRepository.findByUsername(username);
        if (author.isEmpty())
            throw new BadRequestException("Username does not exist");

        Sort.TypedSort<Post> post = Sort.sort(Post.class);
        Sort sort = post.by(Post::getCreatedAt).descending();
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        return postRepository.findByAuthor(author.get(), pageRequest)
                .stream()
                .map(PostsService::wrapPost)
                .toList();
    }

    @Transactional
    public void addPostByUsername(AddPostDto postDto) {
        Post post = unwrapPost(postDto.getPost());
        Optional<Profile> authorOptional = profileRepository.findByUsername(postDto.getProfileUsername());
        if (authorOptional.isEmpty())
            throw new BadRequestException("Username does not exist");

        Profile author = authorOptional.get();
        post.setAuthor(author);
        postRepository.save(post);
        shareSubscribersClient.shareSubscribers(author.getId(), post.getId());
    }

    @Transactional
    public void deletePost(DeletePostDto postDto) {
        Optional<Profile> author = profileRepository.findByUsername(postDto.getUsername());
        if (author.isEmpty())
            throw new BadRequestException("Username does not exist");

        Optional<Post> postToDeleteOptional = postRepository.findById(postDto.getPostId());
        if (postToDeleteOptional.isEmpty())
            throw new BadRequestException("Post does not exist");

        Post postToDelete = postToDeleteOptional.get();
        if (!postToDelete.getAuthor().getId().equals(author.get().getId()))
            throw new BadRequestException("Post does not belong to this profile");

        postToDelete.setAuthor(null);
        postRepository.save(postToDelete);
        shareSubscribersClient.deleteFromSubscribers(postToDelete.getId());
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
