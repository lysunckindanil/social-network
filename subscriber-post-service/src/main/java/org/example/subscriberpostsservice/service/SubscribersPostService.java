package org.example.subscriberpostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.Profile;
import org.example.subscriberpostsservice.repo.PostRepository;
import org.example.subscriberpostsservice.repo.ProfileRepository;
import org.example.subscriberpostsservice.util.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscribersPostService {
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    public List<PostDto> getSubscribersPostsPageable(GetPostsPageableDto dto) {
        String username = dto.getProfileUsername();
        int page = dto.getPage();
        int size = dto.getSize();
        Optional<Profile> author = profileRepository.findByUsername(username);
        if (author.isEmpty()) throw new BadRequestException("Username not found");

        Sort.TypedSort<Post> post = Sort.sort(Post.class);
        Sort sort = post.by(Post::getCreatedAt).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return postRepository.findSubscribedPostsByProfile(author.get(), pageRequest).stream().map(SubscribersPostService::wrapPost).toList();
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
